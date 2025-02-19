package edu.digipen.capstone.equizplatform.services;

import edu.digipen.capstone.equizplatform.entities.*;
import edu.digipen.capstone.equizplatform.exceptions.IllegalAccessException;
import edu.digipen.capstone.equizplatform.models.*;
import edu.digipen.capstone.equizplatform.processors.*;
import edu.digipen.capstone.equizplatform.repositories.*;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.annotation.RequestScope;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Service class that does coordinating between DTOs, Repositories, and Processors.
 */
@AllArgsConstructor
@Service
public class EQuizPlatformService {

    private final QuizRepository quizRepository;

    private final QuestionRepository questionRepository;

    private final TopicRepository topicRepository;

    private final UserRepository userRepository;

    private final CourseRepository courseRepository;

    private final QuizProcessor quizProcessor;
    private final QuestionProcessor questionProcessor;
    private final CurrQuizAttemptProcessor currQuizAttemptProcessor;
    private final QuizAttemptRepository quizAttemptRepository;
    private final QuizQuestionRepository quizQuestionRepository;

    /**
     * Authenticates the user's login credentials by first checking if the userId exists, then checks if password
     * matches between client and database.
     *
     * @param userCredentialsClient - user credential information, containing userId, password (isLecturer is null at
     *                             this point) sent from client side.
     * @return true if the authentication is successful. False otherwise.
     */
    @RequestScope
    public boolean authenticateUserLogin(UserCredentials userCredentialsClient) {

        int clientUserId = userCredentialsClient.getUserId();

        // Check if there is any match for userId
        if (!userRepository.existsById(clientUserId)) {
            System.out.println("User does not exist");
            return false;
        }

        // Retrieves UserCredential item based on user id
        User userDB = userRepository.findById(clientUserId).get();
        UserCredentials userCredentialsDB = new UserCredentials(
                userDB.getUserId(), userDB.getPassword(), userDB.isLecturer());

        String passwordClient = userCredentialsClient.getPassword();
        String passwordDB = userCredentialsDB.getPassword();

        return passwordClient.equals(passwordDB);
    }


    /**
     * Search for the basic user information, which consists of user's first name and last login date.
     *
     * @param userId - the unique user identification number for a given user.
     * @return UserBasicProfileInfo DTO.
     */
    public UserBasicProfileInfo getUserBasicInfo(int userId) {
        User user = userRepository.findById(userId).orElseThrow(() ->
                new IllegalArgumentException("User not found."));

        return new UserBasicProfileInfo(user.getFirstName(),
                                        user.getCourse().getCourseId(),
                                        user.isLecturer(),
                                        user.getLastLoginDate());
    }

    public boolean checkLecturerAccess(int userId) {
        User user = userRepository.findById(userId).orElseThrow(() ->
                new IllegalArgumentException("User not found."));

        return user.isLecturer();
    }

    /**
     * Retrieves a {@code Quiz} object from the repository based on the quiz Id.
     *
     * @param quizId
     * @return
     */
    public Quiz getQuiz(int quizId) {

        return quizRepository.findById(quizId).get();
    }

    /**
     * Launches the Quiz for the user. If the quiz has never been initialised (i.e. temp folder/files not created),
     * this will be done before loading the current quiz attempt.
     *
     * @param userId - the unique user identification number for a given user.
     * @param quizId - the unique quiz identification number for a given quiz.
     * @return
     */
    @RequestScope
    public CurrQuizAttempt launchQuiz(int userId, int quizId) {

        if (!quizRepository.existsById(quizId)) {
            return null;
        }

        Quiz quiz = quizRepository.findById(quizId).orElseThrow(() ->
                new IllegalArgumentException("Quiz not found."));
        QuizAttemptId quizAttemptId = new QuizAttemptId();
        quizAttemptId.setQuizId(quizId);
        quizAttemptId.setUserId(userId);
        QuizAttempt quizAttempt = quizAttemptRepository.findById(quizAttemptId).orElseThrow(() ->
                new IllegalArgumentException("Quiz Attempt not found."));
        System.out.println(quizAttempt.getAttempts());

        // Checks if the max attempt has been reached, or if due date is past
        if (currQuizAttemptProcessor.hasReachedMaxAttempts(quizAttempt, quiz) ||
                quizProcessor.isPastDueDate(quiz.getDueDate())) {
            throw new IllegalAccessException();
        }

        CurrQuizAttempt currQuizAttempt = new CurrQuizAttempt();
        currQuizAttempt.setUserId(userId);
        currQuizAttempt.setQuizId(quizId);
        currQuizAttempt.setQuizName(quiz.getQuizName());
        currQuizAttempt.setCourseName(courseRepository.findById(quiz.getCourseId()).get().getCourseName());
        currQuizAttempt.setCurrQuestion(1);

        // Initialises the Quiz if the quiz is not in progress (need to check quiz progress)
        if (!currQuizAttemptProcessor.quizIsInProgress(userId, quizId)) {
            currQuizAttemptProcessor.initialise(userId, quiz);
        }

        // Loads the quiz
        return currQuizAttemptProcessor.loadProgress(currQuizAttempt);
    }

    /**
     * Gets the next question from a given quiz for a specific user.
     * @param userId - userId for the user who is doing this quiz
     * @param quizId - quizId identifying the quiz which is being done by the user.
     * @param currQuestionNo - current question number based on 1-indexing
     * @return QuestionDto for next question
     */
    // TODO - Check if trying to get next question when current is final question
    public QuestionDto getNextQuestion(int userId, int quizId, int currQuestionNo) {
        int nextQuestionId = currQuizAttemptProcessor.retrieveQuestionId(userId, quizId,
                                            currQuestionNo+1);
        Question nextQuestion = questionRepository.findById(nextQuestionId).orElseThrow(() ->
                new IllegalArgumentException("Question not found."));;

        return questionProcessor.convertDto(nextQuestion);
    }

    /**
     * Gets the previous question from a given quiz for a specific user.
     * @param userId - userId for the user who is doing this quiz
     * @param quizId - quizId identifying the quiz which is being done by the user.
     * @param currQuestionNo - current question number based on 1-indexing
     * @return QuestionDto for previous question
     */
    // TODO - Check if trying to get previous question when current is 1st question
    public QuestionDto getPrevQuestion(int userId, int quizId, int currQuestionNo) {
        int prevQuestionId = currQuizAttemptProcessor.retrieveQuestionId(userId, quizId,
                currQuestionNo-1);
        Question prevQuestion = questionRepository.findById(prevQuestionId).orElseThrow(() ->
                new IllegalArgumentException("Question not found."));;

        return questionProcessor.convertDto(prevQuestion);
    }

    /**
     * Updates the selected choice for a specific question number.
     *
     * <p>
     *     The update is done to the temporary file storage.
     * </p>
     * @param userId - userId for the user who is doing this quiz
     * @param quizId -  quizId identifying the quiz which is being done by the user.
     * @param questionNo - question number for the current quiz attempt.
     * @param choice - selected choice
     * @return true if successful, false otherwise.
     */
    @Transactional
    public boolean updateSelectedChoice(int userId, int quizId, int questionNo, int choice) {
        if (!currQuizAttemptProcessor.updateSelectedChoice(userId, quizId, questionNo, choice)) {
            throw new RuntimeException("Something went wrong with updating selected Choice");
        }
        return true;
    }

    @Transactional
    public QuizResult endQuiz(CurrQuizAttempt currQuizAttempt) {

        // Retrieves the list of answers for questions
        List<QuestionAnswer> questionAnswerList = questionRepository.findAllAnswer(currQuizAttempt.getQuestionOrder());


        int quizId = currQuizAttempt.getQuizId();
        int userId = currQuizAttempt.getUserId();

        // Sets the primary key for the quiz attempt
        QuizAttemptId quizAttemptId = new QuizAttemptId();
        quizAttemptId.setQuizId(quizId);
        quizAttemptId.setUserId(userId);

        QuizAttempt quizAttempt;

        if (!quizAttemptRepository.existsById(quizAttemptId)) {
            return null;
        }
        quizAttempt = quizAttemptRepository.findById(quizAttemptId).orElseThrow(() ->
                new IllegalArgumentException("Quiz Attempt not found."));;

        // set some fields for quiz result
        QuizResult quizResult = new QuizResult(quizId, currQuizAttempt.getQuizName(), 0, questionAnswerList.size(),
                                                new ArrayList<>());

        // Check choices against correct answers, consolidate result and set up quizResult
        int result = currQuizAttemptProcessor.calculateHighScore(currQuizAttempt, questionAnswerList, quizResult);

        // Update score (if high score is lower than current score),  and update attempt count in quizAttempt
        if (quizAttempt.getHighScore() < result) {
            quizAttempt.setHighScore(result);
            quizAttempt.setAttempts(quizAttempt.getAttempts() + 1);
        }

        // Update database
        QuizAttempt savedQuizAttempt = quizAttemptRepository.save(quizAttempt);

        // Delete temp files
        currQuizAttemptProcessor.removeTempFiles(quizId, userId);


        return quizResult;
    }

    /**
     * Adds new quiz into quiz repository, and returns the integer {@code quizId}.
     * This also creates entry for
     *
     * @param newQuiz - QuizCreation DTO object received from client.
     * @return quizId
     */
    @Transactional
    public Integer addNewQuiz(QuizCreation newQuiz) {
        Quiz quiz = new Quiz();
        List<QuizAttempt> quizAttemptList;
        List<QuizQuestion> quizQuestionList;

        // Set fields for Quiz object
        quiz.setQuizName(newQuiz.getQuizName());
        quiz.setMaxAttempts(newQuiz.getMaxAttempts());
        quiz.setCourseId(newQuiz.getCourseId());

        // Throw exception if date given is before today.
        if (LocalDate.now().isAfter(newQuiz.getDueDate())) {
            throw new IllegalArgumentException("Date must be today or later.");
        }
        quiz.setDueDate(newQuiz.getDueDate());

        // Add quiz to repository so that we can get the quizId.
        Quiz savedQuiz = quizRepository.save(quiz);

        // Gets list of questionId
        List<Integer> questionIdList = newQuiz.getSelectedQuestionIdList();

        List<Question> questionList = questionRepository.findAllById(questionIdList);

        // Populate List of QuizQuestion entity then save to repository
        quizQuestionList = quizProcessor.processQuizQuestions(questionList, savedQuiz);
        quizQuestionRepository.saveAll(quizQuestionList);

        // Get and use list of users in a specific course, populate List of QuizAttempt entity then save to repository
        List<User> userList = userRepository.findAllByCourseId(newQuiz.getCourseId());

        quizAttemptList = quizProcessor.processQuizAttempts(userList, savedQuiz);
        quizAttemptRepository.saveAll(quizAttemptList);

        return savedQuiz.getQuizId();
    }

    /**
     * Get question based on the question id, then returns a QuestionDto.
     *
     * @param questionId
     * @return
     */
    public QuestionDto getQuestion(int questionId) {
        Question question = questionRepository.findById(questionId).orElseThrow(() ->
                new IllegalArgumentException("Question not found."));;

        return questionProcessor.convertDto(question);
    }

    public Page<QuestionPreview> getAllQuestionPreview(Integer page, int pageSize) {
        Pageable pageable = PageRequest.of(page, pageSize);
        return questionRepository.findAllDto(pageable);
    }

    public Page<QuestionPreview> getAllQuestionPreview(Integer page) {
        return getAllQuestionPreview(page, 3);
    }

    public Page<QuestionPreview> getAllQuestionsByTopicId(Integer page, int pageSize, Integer topicId) {
        Pageable pageable = PageRequest.of(page, pageSize);
        return questionRepository.findAllByTopicId(pageable, topicId);
    }

    public Page<QuestionPreview> getAllQuestionsByTopicId(int page, int topicId) {
        return getAllQuestionsByTopicId(page, 3, topicId);
    }

    /**
     * Method that updates a question through replacing an existing instance.
     *
     * @param updatedQuestion
     * @return
     */
    @Transactional
    public boolean updateQuestion(Question updatedQuestion) {
        questionRepository.save(updatedQuestion);

        return true;
    }

    /**
     * Method that adds a new question.
     *
     * @param newQuestion
     * @return
     */
    @Transactional
    public boolean addNewQuestion(Question newQuestion) {
        // Ensure the topic is managed by the persistence context
        Topic topic = topicRepository.findById(newQuestion.getTopic().getTopicId())
                .orElseThrow(() -> new IllegalArgumentException("Topic not found"));
        newQuestion.setTopic(topic);
        newQuestion.setTopicId(topic.getTopicId());
        Question savedQuestion = questionRepository.save(newQuestion);

        questionRepository.save(savedQuestion);

        return true;
    }

    public String testAppTemp() {
        return quizProcessor.testAppTemp();
    }

    /**
     *
     * @param quizId
     * @return
     */
    public String getCourseNameByQuizId(int quizId) {
        Course course = courseRepository.findByQuizId(quizId);
        Quiz quiz = quizRepository.findById(quizId).orElseThrow(() ->
                new IllegalArgumentException("Quiz not found."));;

        return course.getCourseName();
    }

    /**
     * Get assigned quiz list for a specific user, with adjustable pageSize.
     *
     * @param userId
     * @param page
     * @param pageSize
     * @return
     */
    public Page<QuizPreview> getAssignedQuizList(int userId, int page, int pageSize) {
        Pageable pageable = PageRequest.of(page, pageSize);
        return quizRepository.findAllByUserId(userId, pageable);
    }

    /**
     * Get assigned quiz list for a specific user, with pageSize fixed to 3.
     *
     * @param userId
     * @param page
     * @return
     */
    public Page<QuizPreview> getAssignedQuizList(int userId, int page) {
        return getAssignedQuizList(userId, page, 3);
    }

    public Page<QuizPreview> getQuizListByCourseId(int courseId, int page, int pageSize) {
        Pageable pageable = PageRequest.of(page, pageSize);
        return quizRepository.findAllByCourseId(courseId, pageable);
    }

    public Page<QuizPreview> getQuizListByCourseId(int courseId, int page) {
        return getQuizListByCourseId(courseId, page, 3);
    }

    public Page<AssignedQuiz> getAssignedQuizByUserId(int userId, int page, int pageSize) {
        Pageable pageable = PageRequest.of(page, pageSize);
        Page<AssignedQuiz> assignedQuizPage = quizRepository.findAllAssignedQuizByUserId(userId, pageable);

        // Set the status of the quiz
        for (AssignedQuiz aq: assignedQuizPage.getContent()) {
            aq.setStatus(currQuizAttemptProcessor.getQuizStatus(aq));
            currQuizAttemptProcessor.setQuizAction(aq);
        }
        return assignedQuizPage;
    }

    public Page<AssignedQuiz> getAssignedQuizByUserId(int userId, int page) {
        return getAssignedQuizByUserId(userId, page, 3);
    }

    public Page<QuizStatus> getQuizStatusByCourseId(int courseId, int page, int pageSize) {
        Pageable pageable = PageRequest.of(page, pageSize);
        Page<QuizStatus> quizStatusList = quizRepository.findAllQuizStatusByUserId(courseId, pageable);
        return quizStatusList;
    }

    public Page<QuizStatus> getQuizStatusByCourseId(int courseId, int page) {
        return getQuizStatusByCourseId(courseId, page, 3);
    }

    /**
     * Get a paginated list of QuestionPreview DTO, with control on page size.
     * @param courseId
     * @param page
     * @param pageSize
     * @return
     */

    public Page<QuestionPreview> getAllQuestionPreviewByCourseId(int courseId, int page, int pageSize) {
        Pageable pageable = PageRequest.of(page, pageSize);
        return questionRepository.findAllDtoByCourseId(pageable, courseId);
    }

    /**
     * Get a paginated list of QuestionPreview DTO, with fixed page size of 3.
     * @param courseId
     * @param page
     * @return
     */

    public Page<QuestionPreview> getAllQuestionPreviewByCourseId(int courseId, int page) {
        return getAllQuestionPreviewByCourseId(courseId, page, 3);
    }
}
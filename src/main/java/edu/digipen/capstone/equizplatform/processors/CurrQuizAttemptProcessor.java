package edu.digipen.capstone.equizplatform.processors;

import edu.digipen.capstone.equizplatform.entities.Quiz;
import edu.digipen.capstone.equizplatform.entities.QuizAttempt;
import edu.digipen.capstone.equizplatform.entities.QuizQuestion;
import edu.digipen.capstone.equizplatform.models.*;
import org.apache.commons.io.FileUtils;
import org.springframework.boot.system.ApplicationTemp;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.*;

/**
 * This class contains methods that processes the current quiz attempt for a specific quiz and user, such as
 * creation, access, update and deletion of temp files used, as well as various helper methods involved.
 */
@Component
public class CurrQuizAttemptProcessor {

    public String getQuizStatus(AssignedQuiz assignedQuiz) {

        // First, check if the quiz past due date
        if (assignedQuiz.getDueDate().isBefore(LocalDate.now())) {
            return "ENDED";
        }

        // Then, check for if an ongoing attempt is present
        if (quizIsInProgress(assignedQuiz.getUserId(), assignedQuiz.getQuizId())) {
            return "IN PROGRESS";
        }
        // Next, check for if any attempts has been made in the first place
        if (isNotAttempted(assignedQuiz)) {
            return "NOT STARTED";
        }
        // This leaves us with the scenario the user has completed the quiz
        return "COMPLETED";
    }

    public void setQuizAction(AssignedQuiz assignedQuiz) {

        String status = assignedQuiz.getStatus();
        LocalDate dueDate = assignedQuiz.getDueDate();
        int attempts = assignedQuiz.getAttempts();
        int maxAttempts = assignedQuiz.getMaxAttempts();

        if (status.equalsIgnoreCase("ENDED") ||
            attempts >= maxAttempts) {
            assignedQuiz.setAction("NO ACTION POSSIBLE");
        }

        else if (status.equalsIgnoreCase("IN PROGRESS")) {
            assignedQuiz.setAction("RESUME");
        }
        else if (status.equalsIgnoreCase("NOT STARTED") ||
            status.equalsIgnoreCase("COMPLETED") && dueDate.isAfter(LocalDate.now())) {
            assignedQuiz.setAction("START");
        }
    }

    public int retrieveQuestionId(int userId, int quizId, int questionIndexToLoad) {
        ApplicationTemp appTemp = new ApplicationTemp();
        String subDir = String.join("",Integer.toString(userId), "/", Integer.toString(quizId));
        Path canonPath;
        List<String> textDataStore;
        String currLine;
        String[] splitCurrLine;
        int questionId = -1;

        subDir = String.join("", appTemp.getDir().toString(),"/", subDir, "/questions.txt");

        try {
            // Navigate to path for current question, open file and load into memory
            canonPath = appTemp.getDir(subDir).toPath().toAbsolutePath();
            textDataStore = Files.readAllLines(canonPath);

            // Get current question
            currLine = textDataStore.get(questionIndexToLoad);

            // Split line to "questionId, choice"
            splitCurrLine = currLine.split(",");

            // Load question
            return Integer.parseInt(splitCurrLine[0]);

        }
        catch (IOException ioe) {
            System.out.println(ioe.getMessage());
        }
        catch (NumberFormatException nfe) {
            System.out.println(nfe.getMessage() + nfe.getCause());
        }

        return questionId;
    }

    /**
     * Checks if the quiz has currently never been completed by a particular user.
     *
     * @param quizAttempt {@code QuizAttempt} object
     * @return true if no. of attempts is 0, false otherwise.
     */
    public boolean isNotAttempted(QuizAttempt quizAttempt) {
        return quizAttempt.getAttempts() == 0;
    }

    /**
     * Checks if the quiz has currently never been completed by a particular user.
     *
     * @param assignedQuiz {@code AssignedQuiz} object
     * @return true if no. of attempts is 0, false otherwise.
     */
    public boolean isNotAttempted(AssignedQuiz assignedQuiz) {
        return assignedQuiz.getAttempts() == 0;
    }

    /**
     * Checks if the quiz has reached maximum allowable attempts
     *
     * @param quizAttempt {@code QuizAttempt} object
     * @param quiz {@code Quiz} object
     * @return true if reached max attempts, false otherwise
     */
    public boolean hasReachedMaxAttempts(QuizAttempt quizAttempt, Quiz quiz) {
        return quizAttempt.getAttempts() >= quiz.getMaxAttempts();
    }


    /**
     * Checks if the quiz is being attempted, by checking if the temporary quiz folder exists.
     *
     * @param userId
     * @param quizId
     * @return
     */
    public boolean quizIsInProgress(int userId, int quizId) {
        ApplicationTemp appTemp = new ApplicationTemp();
        String subDir = String.join("",Integer.toString(userId), "/", Integer.toString(quizId));
        Path basePath = appTemp.getDir().toPath();


        return Files.exists(Paths.get(basePath.toAbsolutePath().toString(), subDir));

    }

    /**
     * Helper method that randomises the order of the questions, returning a list of question id in randomised order.
     *
     * The list of questions is iterated through in the order given through the argument, extracting the Question id
     * into a linked list. Thereafter, a random number between 0 and the remaining length of the list is generated.
     * The question id is removed from the linked list and then added to the randomised list. Each time this is done,
     * the seed is decreased by one, until the linked list is empty.
     *
     * @param questionList - {@code List<Question>} to process a new randomised order for.
     * @return {@code List<Integer>} containing question id in randomised order
     */
    private List<Integer> randomiseQuizQuestionOrder(List<QuizQuestion> questionList) {

        List<Integer> reorderedQuestionOrder = new ArrayList<>();
        List<Integer> questionIdList = new LinkedList<>();
        int questionCount = questionList.size();
        int randQuestionId;
        Random random = new Random();

        // Loads the original list order into the linked list
        for (int i=0; i < questionCount; i++) {
            questionIdList.add(questionList.get(i).getQuestionId());
        }

        // Randomly selects a question id and add to reordered list, then reduces the pool of remaining question id
        while (!questionIdList.isEmpty()) {
            randQuestionId = questionIdList.remove(random.nextInt(questionCount)); // this picks from 0... (n-1)
            reorderedQuestionOrder.add(randQuestionId);
            questionCount--;
        }

        return reorderedQuestionOrder;
    }


    /**
     * Initialises the quiz attempt, through creation of files in the temporary folder.
     * <br/>
     * <p>
     *     This creates the temporary directory and files in the directory designated by the {@code getDir()} void
     *     method from {@code ApplicationTemp} class.
     * <ul>
     *     <li>The temporary directory created is /(userId)/(quizId of the Quiz)/.</li>
     *     <li>The question temp file will be designated "questions.txt"</li>
     *     <li>Inside each questions.txt will be initially written with the questionId that identifies the
     *     actual question per line. The order is sequential (e.g. first line is question 1).</li>
     * </ul>
     * </p>
     * @param userId
     * @param quiz
     */
    public void initialise(int userId, Quiz quiz) {

        // Get the Quiz ID of the current quiz
        int quizId = quiz.getQuizId();

        // Get the list of questions from the quiz
        List<QuizQuestion> questions = quiz.getQuestionList();

        // Create new directory for the current quiz
        ApplicationTemp appTemp = new ApplicationTemp();
        Path basePath = appTemp.getDir().toPath();
        String subDir= String.join("",Integer.toString(userId), "/", Integer.toString(quizId));
        Path newPath = Paths.get(basePath.toAbsolutePath().toString(), subDir);

        // Ensure the directory exists
        try {
            Files.createDirectories(newPath);

            // prematurely exits the method if IOException is caught
        } catch (IOException e) {
            System.err.println("Failed to create directory: " + e.getMessage());
            return;
        }

        Path path = appTemp.getDir(subDir).toPath();

        // Randomise question order before loading questions into temp files
        List<Integer> randomisedQuestionOrder = randomiseQuizQuestionOrder(questions);

        // sets the path for the new question text file to be created in
        String qnPath = String.join("",path.toString(), "/questions.txt");
        Path newQnPath = Paths.get(qnPath);

        try {
            // Create questions text file, then convert this file path to canonical path
            Files.createFile(newQnPath);
            Path canonPath = newQnPath.toAbsolutePath();
            List<String> questionList = new ArrayList<>();

            for (int i=0; i < randomisedQuestionOrder.size(); i++) {

                // For each line, fill with "questionId, 0".
                // Filling 0 ensures splitting string by ',' gives capacity 2 String arrays
                String currQuestionChoice = String.join(",",
                        Integer.toString(randomisedQuestionOrder.get(i)),
                        "0");

                // Fills String form of questionId into each index of questionList
                questionList.add(currQuestionChoice);
            }

            // write all lines from questionList into questions.txt
            Files.write(canonPath, questionList, StandardCharsets.UTF_8);

        } catch (IOException ioe) {
            System.out.println("Failed to create or write to file: " + ioe.getMessage());
        }

        System.out.println("Initialisation completed.");
    }

    /**
     * Loads progress from temporary file storage into a CurrQuizAttempt object and returns it.
     *
     * @param currQuizAttempt
     * @return
     */
    public CurrQuizAttempt loadProgress(CurrQuizAttempt currQuizAttempt) {
        ApplicationTemp appTemp = new ApplicationTemp();
        int userId = currQuizAttempt.getUserId();
        int quizId = currQuizAttempt.getQuizId();
        String subDir = String.join("",Integer.toString(userId), "/", Integer.toString(quizId));
        Path basePath = appTemp.getDir().toPath();
        Path canonPath;
        List<String> questionList;

        // This will contain [questionId, answer] in each line.
        List<Integer> selectedChoices = new ArrayList<>();
        List<Integer> questionOrder = new ArrayList<>();

        // Concatenates the subdirectory for the current question. Also
        // assigns 1-indexing to match the question no.
        subDir = String.join("", subDir, "/questions.txt");
        canonPath = Paths.get(basePath.toString(), subDir);

        try {
            // loads in values for questions into questionList
            questionList = Files.readAllLines(canonPath);

            // iterates through the question index
            for (int i=0; i < questionList.size(); i++) {

                // get current line at the index and splits based on delimiter (will always be capacity 2)
                String[] currLine = questionList.get(i).split(",");
                System.out.println(Arrays.toString(currLine));
                questionOrder.add(Integer.parseInt(currLine[0]));
                selectedChoices.add(Integer.parseInt(currLine[1]));

            }

            // set questionsAnswer into the DTO
            currQuizAttempt.setSelectedChoices(selectedChoices);
            currQuizAttempt.setQuestionOrder(questionOrder);
        }
        catch (IOException ioe) {
            System.out.println("IOE Ln 251" + ioe.getMessage());
        }
        catch (NumberFormatException nfe) {
            System.out.println("NFE Ln 254");
            System.err.println(nfe);
        }

        return currQuizAttempt;
    }

    /**
     * Updates the current question in the current quiz attempted with the user selected choice.
     *
     * @param userId
     * @param quizId
     * @param questionNo
     * @param choice
     * @return
     */
    public boolean updateSelectedChoice(int userId, int quizId, int questionNo, int choice) {

        ApplicationTemp appTemp = new ApplicationTemp();
        String subDir = String.join("", appTemp.getDir().getAbsolutePath(), "/",
                Integer.toString(userId), "/", Integer.toString(quizId),
                "/questions.txt");
        Path path = appTemp.getDir(subDir).toPath().toAbsolutePath();
        List<String> textFileContents;

        // questionNo (1-indexing) is converted to 0-indexing here
        int questionIndex = questionNo - 1;

        String currLine;
        String[] splitCurrLine;

        try {
            // Navigate to path for current question, open file and load into memory
            textFileContents = Files.readAllLines(path);
            currLine = textFileContents.get(questionIndex);

            // Split the line for containing questionId and current selected choice, based on the delimiter
            splitCurrLine = currLine.split(",");

            // Update the choice
            splitCurrLine[1] = Integer.toString(choice);

            // Rejoin the string
            currLine = String.join(",", splitCurrLine[0], splitCurrLine[1]);

            // Write back into the questionIndex
            textFileContents.set(questionIndex, currLine);

            // Overwrites the contents in the text file
            Files.write(path, textFileContents);
            return true;
        }
        catch (IOException ioe) {

            return false;
        }
    }

    /**
     * Remove the temp folder (and in effect, all the files therein) after quiz is completed.
     *
     * @param quizId
     * @param userId
     * @return true if the removal of temp files is successful, false otherwise.
     */
    public boolean removeTempFiles(int quizId, int userId) {
        ApplicationTemp appTemp = new ApplicationTemp();

        // Set subdirectory for the quiz folder
        String subDir = String.join("", Integer.toString(userId), "/", Integer.toString(quizId));
        Path path = appTemp.getDir(subDir).toPath().toAbsolutePath();
        try {
            // Deletes the quizId folder and its files.
            FileUtils.forceDelete(path.toFile());
            return true;
        }
        catch (IOException ioe) {
            return false;
        }
    }

    /**
     * Calculates the high score for a completed quiz attempt. The method not only returns the calculated score, but
     * also mutates the {@code QuizResult} object.
     *
     * @param currQuizAttempt
     * @param questionAnswerList
     * @param quizResult - the {@code QuizResult} object to be mutated.
     * @return
     */
    public int calculateHighScore(CurrQuizAttempt currQuizAttempt, List<QuestionAnswer> questionAnswerList,
                                  QuizResult quizResult) {

        List<Integer> questionOrder = currQuizAttempt.getQuestionOrder();
        List<Integer> selectedChoiceList = currQuizAttempt.getSelectedChoices();
        List<Boolean> resultList = new ArrayList<>();
        int score = 0;

        /*
         * questionId - questionId from questionOrder.
         * selectedChoice - selected choice for a particular question.
         * ansQuestionid - the questionId extracted from an element of the questionAnswerList
         * currCorrectAns - the answerId extracted from an element of the questionAnswerList
         */
        int questionId, selectedChoice, ansQuestionId, currCorrectAns;
        Map<Integer, Integer> selectedChoicesMap = new HashMap<>();


        for (int i=0; i < questionOrder.size(); i++) {

            // Get the questionId-selectedChoice mapped together, since they are guaranteed to be correctly indexed
            questionId = questionOrder.get(i);
            selectedChoice = selectedChoiceList.get(i);
            selectedChoicesMap.put(questionId, selectedChoice);
        }

        for (int i=0; i < questionOrder.size(); i++) {
            // Get scoring for a particular questionId
            ansQuestionId = questionAnswerList.get(i).getQuestionId();
            currCorrectAns = questionAnswerList.get(i).getAnswerId();

            // tally score
            if (currCorrectAns == selectedChoicesMap.get(ansQuestionId)) {
                score += 1;
                resultList.add(true);
            }
            else {
                resultList.add(false);
            }
        }

        // Modifies quizResult
        quizResult.setResults(resultList);
        quizResult.setTotalQuestions(questionOrder.size());
        quizResult.setScore(score);

        return score;
    }
}

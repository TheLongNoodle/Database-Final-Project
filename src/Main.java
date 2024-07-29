import java.sql.*;
import java.util.InputMismatchException;
import java.util.Scanner;

public class Main {

    private static Statement stmt;
    private static Scanner sc;
    private static int SID;
    private static SubjectPool subjectPool;
    private static QuestionPool questionPool;
    private static AnswerPool answerPool;

    public static void main(String[] args) throws SQLException {

        // Connecting to database
        try {
            Connection con = DriverManager.getConnection("jdbc:postgresql://localhost:5432/examdb", "postgres", "admin");
            System.out.println("Connected to database!");
            stmt = con.createStatement();

            // initializing
            subjectPool = new SubjectPool(con);
            questionPool = new QuestionPool(con);
            answerPool = new AnswerPool(con);

        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }

        SID = 0;
        sc = new Scanner(System.in);
        int choice;
        boolean exit = false;

        if (subjectPool.getSubjectsLen() == 0) {
            System.out.println("There are no subjects, please create a new one");
            SID = createSubject();
            questionPool.setSID(SID);
            answerPool.setSID(SID);
        } else {
            System.out.println("1: choose existing subject.\n2: create new subject");
            choice = sc.nextInt();
            switch (choice) { // menu
                case 1: { // choosing existing subject
                    sc.nextLine();
                    SID = changeSubject();
                    questionPool.setSID(SID);
                    answerPool.setSID(SID);
                    break;
                }
                case 2: { // creating new subject
                    sc.nextLine();
                    SID = createSubject();
                    questionPool.setSID(SID);
                    answerPool.setSID(SID);
                    break;
                }
                default: { // validating input
                    System.out.println("Incorrect input, try again.");
                    sc.nextLine();
                    break;
                }
            }
        }
        System.out.println("Welcome to my test GUI! \n");
        while (true) {
            try {
                System.out.println("Current subject: " + subjectPool.getSubjectName(SID));
                System.out.println(
                        "1: question related.\n2: answer related.\n3: create exam.\n4: subject related.\n5: print all questions with answers.\n0: exit.");
                choice = sc.nextInt();

                switch (choice) { // menu

                    case 1: {
                        while (true) {
                            System.out.println(
                                    "1: add question to pool.\n2: remove question from pool.\n3: change question.\n4: print all questions.\n0: go back.");
                            choice = sc.nextInt();
                            switch (choice) { // sub menu: question
                                case 1: { // adding question to pool
                                    sc.nextLine();
                                    addQuestion();
                                    exit = true;
                                    break;
                                }
                                case 2: { // deleting question from pool
                                    sc.nextLine();
                                    deleteQuestion();
                                    sc.nextLine();
                                    exit = true;
                                    break;
                                }
                                case 3: { // change question
                                    sc.nextLine();
                                    changeQuestion();
                                    exit = true;
                                    break;
                                }
                                case 4: { // print all questions
                                    sc.nextLine();
                                    printAllQuestions();
                                    exit = true;
                                    break;
                                }
                                case 0: { // go back
                                    sc.nextLine();
                                    exit = true;
                                    break;
                                }
                                default: { // validating input
                                    sc.nextLine();
                                    System.out.println("Incorrect input, try again.");
                                    break;
                                }
                            }
                            if (exit) { // exit loop condition
                                exit = false;
                                break;
                            }
                        }
                        break;
                    }

                    case 2: {
                        while (true) {
                            System.out.println(
                                    "1: add answer to pool.\n2: add answer to question.\n3: remove answer from pool.\n4: remove answer from question.\n5: change answer.\n6: change open answer.\n7: print all answers\n0: go back.");
                            choice = sc.nextInt();

                            switch (choice) { // sub menu: answer
                                case 1: { // adding answer to pool
                                    sc.nextLine();
                                    addAnswerToPool();
                                    exit = true;
                                    break;
                                }
                                case 2: { // adding answer to question
                                    sc.nextLine();
                                    addAnswerToQuestion();
                                    exit = true;
                                    break;
                                }
                                case 3: { // deleting answer from pool
                                    sc.nextLine();
                                    deleteAnswerFromPool();
                                    exit = true;
                                    break;
                                }

                                case 4: { // deleting answer from question
                                    sc.nextLine();
                                    deleteAnswerFromQuestion();
                                    exit = true;
                                    break;
                                }
                                case 5: { // changing answer content
                                    sc.nextLine();
                                    changAnswerContent();
                                    sc.nextLine();
                                    exit = true;
                                    break;
                                }
                                case 6: { // change open question answer
                                    sc.nextLine();
                                    changeOpenAnswer();
                                    exit = true;
                                    break;
                                }
                                case 7: { // print all answers
                                    sc.nextLine();
                                    printAnswers();
                                    exit = true;
                                    break;
                                }
                                case 0: { // go back
                                    sc.nextLine();
                                    exit = true;
                                    break;
                                }
                                default: { // validating input
                                    sc.nextLine();
                                    System.out.println("Incorrect input, try again.");
                                    break;
                                }
                            }
                            if (exit) { // exit loop condition
                                exit = false;
                                break;
                            }
                        }
                        break;
                    }
                    case 3: { // create exam
                        //TODO: create exam();
                    }

                    case 4: { // print all questions with answers
                        while (true) {
                            System.out.println(
                                    "1: create subject.\n2: change subject.\n3: change subject name.\n0: go back.");
                            choice = sc.nextInt();
                            switch (choice) {
                                case 1: { // create subject
                                    sc.nextLine();
                                    SID = createSubject();
                                    questionPool.setSID(SID);
                                    answerPool.setSID(SID);
                                    exit = true;
                                    break;
                                }
                                case 2: { // change subject
                                    sc.nextLine();
                                    SID = changeSubject();
                                    questionPool.setSID(SID);
                                    answerPool.setSID(SID);
                                    exit = true;
                                    break;
                                }
                                case 3: { // change subject names
                                    sc.nextLine();
                                    changeSubjectName();
                                    exit = true;
                                    break;
                                }
                                case 0: { // go back
                                    sc.nextLine();
                                    exit = true;
                                    break;
                                }
                                default: { // validating input
                                    sc.nextLine();
                                    System.out.println("Incorrect input, try again.");
                                    break;
                                }
                            }
                            if (exit) { // exit loop condition
                                exit = false;
                                break;
                            }
                        }
                        break;
                    }
                    case 5: { // print all questions with answers
                        printAll();
                        break;
                    }
                    case 0: { // exit
                        exit = true;
                        break;
                    }
                    default: { // validating input
                        System.out.println("Incorrect input, try again.");
                        break;
                    }
                }
                if (exit) { // exit loop condition
                    break;
                }
            } catch (InputMismatchException e) {
                sc.nextLine();
                System.out.println("That's the wrong kind of input, aborting...");
            }
        }
    }

    private static void changeSubjectName() throws SQLException {
        System.out.println("Current name: " + subjectPool.getSubjectName(SID));
        System.out.print("Enter new name: ");
        subjectPool.setSubjectName(SID, sc.nextLine());
    }

    private static int changeSubject() throws SQLException {
        System.out.println(subjectPool.toString());
        System.out.print("Enter subject number: ");
        while (true) {
            int choice = sc.nextInt();
            String query = "SELECT * FROM subject WHERE sid = " + choice;
            ResultSet resultSet = stmt.executeQuery(query);
            if (resultSet.next()) {
                return choice;
            }
            System.out.println("Wrong input, try again");
        }
    }

    private static int createSubject() throws SQLException {
        System.out.print("Enter subject name: ");
        String name = sc.nextLine();
        return subjectPool.addSubject(name);

    }

    private static void addQuestion() throws SQLException {
        System.out.print("Enter question here: ");
        String content = sc.nextLine();
        int difficulty;
        while (true) { // validating input
            System.out.print("Enter difficulty (1-3): ");
            difficulty = sc.nextInt();
            if ((difficulty >= 1) && (difficulty <= 3)) {
                break;
            }
            System.out.println("number have to be between 1 and 3");
        }
        boolean bool;
        System.out.print("Open question? [true/false]: ");
        bool = sc.nextBoolean();
        if (bool) {
            sc.nextLine();
            System.out.print("Enter answer here: ");
            questionPool.addQuestion(content, difficulty, sc.nextLine(), false);
        } else {
            questionPool.addQuestion(content, difficulty, "", true);
        }
        System.out.println("Finished");
    }

    private static void deleteQuestion() throws SQLException {
        if (questionPool.hasQuestions()) { // checking if there are questions
            System.out.println(questionPool);
            int question = chooseQuestion();
            questionPool.deleteQuestion(question);
            System.out.println("Finished");
        } else {
            System.out.println("There are no questions to delete, aborting...");
        }
    }

    private static void changeQuestion() throws SQLException {
        if (questionPool.hasQuestions()) { // checking if there are questions
            System.out.println(questionPool);
            int question = chooseQuestion();
            sc.nextLine();
            System.out.print("Enter question content: ");
            questionPool.changeQuestionContent(question, sc.nextLine());
            System.out.println("Finished");
        } else {
            System.out.println("There are no questions to change, aborting...");
        }
    }

    private static void printAllQuestions() throws SQLException {
        if (questionPool.hasQuestions()) { // checking if there are questions
            System.out.println(questionPool);
        } else {
            System.out.println("There are no questions, aborting...");
        }
    }

    private static void addAnswerToPool() throws SQLException {
        System.out.print("Enter answer here: ");
        String content = sc.nextLine();
        answerPool.addAnswer(content);
        System.out.println("Finished");
    }

    private static void addAnswerToQuestion() throws SQLException {
        if ((answerPool.hasAnswers()) && (questionPool.hasSelectionQuestions())) { // checking if there are selection
            // questions/answers
            System.out.println(questionPool.toStringSelection());
            int question = chooseSelectionQuestion();
            System.out.println(answerPool);
            int answer = chooseAnswer();
            System.out.print("Is the answer correct?[true/false]: ");
            Boolean bool = sc.nextBoolean();
            questionPool.addAnswer(question, answer, bool);
            System.out.println("Finished");
        } else {
            System.out.println("There are no selection questions and/or answers, aborting...");
        }
    }

    private static void deleteAnswerFromPool() throws SQLException {
        if (answerPool.hasAnswers()) { // checking if there are answers
            System.out.println(answerPool);
            int answer = chooseAnswer();
            answerPool.deleteAnswer(answer);
            System.out.println("Finished");
        } else {
            System.out.println("There are no answers to delete, aborting...");
        }
    }

    private static void deleteAnswerFromQuestion() throws SQLException {
        if (questionPool.hasSelectionQuestions()) { // checking if there are selection questions
            System.out.println(questionPool.toStringSelection());
            int question = chooseSelectionQuestion();
            if (questionPool.hasAnswers(question)) { // checking if the selection question has answers
                System.out.println(questionPool.printAnswers(question));
                int answer = chooseAnswer();
                questionPool.deleteAnswer(question, answer);
                System.out.println("Finished");
            } else {
                System.out.println("There are no answers in that question, aborting...");
            }
        } else {
            System.out.println("There are no selection questions to delete from, aborting...");
        }
    }

    private static void changAnswerContent() throws SQLException {
        if (answerPool.hasAnswers()) { // checking if there are answers
            System.out.println(answerPool);
            int answer = chooseAnswer();
            System.out.print("Enter answer content: ");
            String content = sc.nextLine();
            answerPool.changeAnswerContent(answer, content);
            System.out.println("Finished");
        } else {
            System.out.println("There are no answers to change, aborting...");
        }
    }

    private static void changeOpenAnswer() throws SQLException {
        if (questionPool.hasOpenQuestions()) { // checking if there are open questions
            System.out.println(questionPool.toStringOpen());
            int question;
            String content;
            while (true) { // validating input
                System.out.print("Choose the number of the open question you wish to change its answer: ");
                question = sc.nextInt();
                sc.nextLine();
                if ((question > 0) && (question < questionPool.getQuestionsLen())
                        && (questionPool.isOpen(question))) {
                    System.out.print("Enter answer content: ");
                    content = sc.nextLine();
                    break;
                }
                System.out.println("Incorrect input, try again...");
            }
            questionPool.setAnswer(content, question);
            System.out.println("Finished");
        } else {
            System.out.println("There are no open questions to change, aborting...");
        }
    }

    private static void printAnswers() throws SQLException {
        if (answerPool.hasAnswers()) { // checking if there are answers
            System.out.println(answerPool);

        } else {
            System.out.println("There are no answers, aborting...");

        }
    }

    private static void printAll() throws SQLException {
        if (questionPool.hasQuestions()) { // checking if there are questions
            System.out.println(questionPool.toStringFull());

        } else {
            System.out.println("There are no questions, aborting...");

        }
    }

    // choose answer
    public static int chooseAnswer() throws SQLException {
        int answer;
        while (true) { // validating input
            System.out.print("Choose the number of the answer you wish to select: ");
            answer = sc.nextInt();
            sc.nextLine();
            String query = "SELECT aid FROM answer WHERE aid = " + answer + " AND sid = " + SID;
            ResultSet resultSet = stmt.executeQuery(query);
            if (resultSet.next()) {
                break;
            }
            System.out.println("Incorrect input, try again...");
        }
        return answer;
    }

    // choose question
    public static int chooseSelectionQuestion() throws SQLException {
        int question;
        while (true) { // validating input
            System.out.print("Choose the number of the question you wish to select: ");
            question = sc.nextInt();
            String query = "SELECT qid FROM question WHERE question.qid = " + question + " AND question.sid = " + SID + " AND is_selection = TRUE";
            ResultSet resultSet = stmt.executeQuery(query);
            if (resultSet.next()) {
                break;
            }
            System.out.println("Incorrect input, try again...");
        }
        return question;
    }

    public static int chooseQuestion() throws SQLException {
        int question;
        while (true) { // validating input
            System.out.print("Choose the number of the question you wish to select: ");
            question = sc.nextInt();
            String query = "SELECT qid FROM question WHERE qid = " + question + " AND sid = " + SID;
            ResultSet resultSet = stmt.executeQuery(query);
            if (resultSet.next()) {
                break;
            }
            System.out.println("Incorrect input, try again...");
        }
        return question;
    }
}

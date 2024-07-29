import java.io.*;
import java.util.InputMismatchException;
import java.util.Objects;
import java.util.Random;
import java.util.Scanner;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class Main {

    public static void main(String[] args) {

        // Connecting to database
        try {
            Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/examdb", "postgres", "admin");
            System.out.println("Connected to database!");
            Statement statement = connection.createStatement();

            // Execute a query
            String query = "SELECT * FROM exam";
            ResultSet resultSet = statement.executeQuery(query);
            while (resultSet.next()) {
                System.out.println(resultSet.getString("EID"));
            }


            // loading data and initializing
            int currentSubject = 0;
            SubjectPool subjectPool = new SubjectPool();
            QuestionPool questionPool = new QuestionPool();
            AnswerPool answerPool = new AnswerPool();
            Scanner sc = new Scanner(System.in);
            int choice;
            boolean exit = false;
            try {
                ObjectInputStream inFile = new ObjectInputStream(new FileInputStream("src\\SubjectPool.dat"));
                subjectPool = (SubjectPool) inFile.readObject();
                inFile.close();
//			}
            } catch (FileNotFoundException e) {
                System.out.println("File \"src\\SubjectPool.dat\" not found, starting a new pool...");
            }

            if (subjectPool.getSubjects().length <= 1) {
                System.out.println("There are no subjects, please create a new one");
                currentSubject = createSubject(sc, subjectPool);
                questionPool = subjectPool.getQuestionPool(currentSubject);
                answerPool = subjectPool.getAnswerPool(currentSubject);
            } else {
                System.out.println("1: choose existing subject.\n2: create new subject");
                choice = sc.nextInt();
                switch (choice) { // menu
                    case 1: { // choosing existing subject
                        sc.nextLine();
                        currentSubject = changeSubject(sc, subjectPool, currentSubject, questionPool, answerPool);
                        questionPool = subjectPool.getQuestionPool(currentSubject);
                        answerPool = subjectPool.getAnswerPool(currentSubject);
                        break;
                    }
                    case 2: { // creating new subject
                        sc.nextLine();
                        currentSubject = createSubject(sc, subjectPool);
                        questionPool = subjectPool.getQuestionPool(currentSubject);
                        answerPool = subjectPool.getAnswerPool(currentSubject);
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
                    System.out.println("Current subject: " + subjectPool.getSubject(currentSubject).getName());
                    System.out.println(
                            "1: question related.\n2: answer related.\n3: exam related.\n4: subject related.\n5: print all questions with answers.\n0: exit.");
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
                                        addQuestion(sc, questionPool);
                                        exit = true;
                                        break;
                                    }
                                    case 2: { // deleting question from pool
                                        sc.nextLine();
                                        deleteQuestion(sc, questionPool);
                                        sc.nextLine();
                                        exit = true;
                                        break;
                                    }
                                    case 3: { // change question
                                        sc.nextLine();
                                        changeQuestion(sc, questionPool);
                                        exit = true;
                                        break;
                                    }
                                    case 4: { // print all questions
                                        sc.nextLine();
                                        printAllQuestions(questionPool);
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
                                        addAnswerToPool(sc, answerPool);
                                        exit = true;
                                        break;
                                    }
                                    case 2: { // adding answer to question
                                        sc.nextLine();
                                        addAnswerToQuestion(sc, questionPool, answerPool);
                                        exit = true;
                                        break;
                                    }
                                    case 3: { // deleting answer from pool
                                        sc.nextLine();
                                        deleteAnswerFromPool(sc, answerPool);
                                        exit = true;
                                        break;
                                    }

                                    case 4: { // deleting answer from question
                                        sc.nextLine();
                                        deleteAnswerFromQuestion(sc, questionPool, answerPool);
                                        exit = true;
                                        break;
                                    }
                                    case 5: { // changing answer content
                                        sc.nextLine();
                                        changAnswerContent(sc, answerPool);
                                        sc.nextLine();
                                        exit = true;
                                        break;
                                    }
                                    case 6: { // change open question answer
                                        sc.nextLine();
                                        changeOpenAnswer(sc, questionPool);
                                        exit = true;
                                        break;
                                    }
                                    case 7: { // print all answers
                                        sc.nextLine();
                                        printAnswers(answerPool);
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
                        case 3: { // sub menu: test
                            while (true) {
                                System.out.println("1: create manual exam.\n2: create automatic exam.\n0: exit.");
                                choice = sc.nextInt();
                                switch (choice) {
                                    case 1: { // make manual test
                                        makeManualExam(sc, questionPool);
                                        exit = true;
                                        break;
                                    }
                                    case 2: { // make automatic test
                                        sc.nextLine();
                                        makeAutomaticExam(sc, questionPool);
                                        exit = true;
                                        break;
                                    }
                                    case 0: { // exit
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
                                    sc.nextLine();
                                    exit = false;
                                    break;
                                }
                            }
                            break;
                        }

                        case 4: { // print all questions with answers
                            while (true) {
                                System.out.println(
                                        "1: create subject.\n2: change subject.\n3: change subject name.\n0: go back.");
                                choice = sc.nextInt();
                                switch (choice) {
                                    case 1: { // create subject
                                        sc.nextLine();
                                        currentSubject = createSubject(sc, subjectPool);
                                        questionPool = subjectPool.getQuestionPool(currentSubject);
                                        answerPool = subjectPool.getAnswerPool(currentSubject);
                                        exit = true;
                                        break;
                                    }
                                    case 2: { // change subject
                                        sc.nextLine();
                                        currentSubject = changeSubject(sc, subjectPool, currentSubject, questionPool, answerPool);
                                        questionPool = subjectPool.getQuestionPool(currentSubject);
                                        answerPool = subjectPool.getAnswerPool(currentSubject);
                                        exit = true;
                                        break;
                                    }
                                    case 3: { // change subject names
                                        sc.nextLine();
                                        changeSubjectName(sc, subjectPool, currentSubject);
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
                            printAll(questionPool);
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

            // saving data
            subjectPool.setQuestionPool(questionPool, currentSubject);
            subjectPool.setAnswerPool(answerPool, currentSubject);
            ObjectOutputStream outFile = new ObjectOutputStream(new FileOutputStream("src\\SubjectPool.dat"));
            outFile.writeObject(subjectPool);
            outFile.close();
            sc.close();
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    private static void changeSubjectName(Scanner sc, SubjectPool subjectPool, int currentSubject) {
        System.out.println("Current name: " + subjectPool.getSubject(currentSubject).getName());
        System.out.print("Enter new name: ");
        subjectPool.getSubject(currentSubject).setName(sc.nextLine());
    }

    private static int changeSubject(Scanner sc, SubjectPool subjectPool, int currentSubject, QuestionPool questionPool,
                                     AnswerPool answerPool) {
        System.out.println(subjectPool.toString());
        System.out.print("Enter subject number: ");
        while (true) {
            int choice = sc.nextInt();
            sc.nextLine();
            if ((choice > 0) && (choice < subjectPool.getSubjects().length)) {
                if (currentSubject != 0) {
                    subjectPool.setQuestionPool(questionPool, currentSubject);
                    subjectPool.setAnswerPool(answerPool, currentSubject);
                }
                return choice;
            }
            System.out.println("Wrong input, try again");
        }
    }

    private static int createSubject(Scanner sc, SubjectPool subjectPool) {
        System.out.print("Enter subject name: ");
        String name = sc.nextLine();
        return subjectPool.addSubject(name);

    }

    private static void addQuestion(Scanner sc, QuestionPool questionPool) {
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
            questionPool.addQuestion(content, difficulty, sc.nextLine());
        } else {
            questionPool.addQuestion(content, difficulty);
        }
        System.out.println("Finished");
    }

    private static void deleteQuestion(Scanner sc, QuestionPool questionPool) {
        if (questionPool.hasQuestions()) { // checking if there are questions
            System.out.println(questionPool);
            int question;
            while (true) { // validating input
                System.out.print("Choose the number of the question you wish to delete: ");
                question = sc.nextInt();
                if ((question > 0) && (question < questionPool.getQuestions().length)) {
                    break;
                }
                System.out.println("Incorrect input, try again...");
            }
            questionPool.deleteQuestion(question);
            System.out.println("Finished");
        } else {
            System.out.println("There are no questions to delete, aborting...");
        }
    }

    private static void changeQuestion(Scanner sc, QuestionPool questionPool) {
        if (questionPool.hasQuestions()) { // checking if there are questions
            System.out.println(questionPool);
            int question;
            while (true) { // validating input
                System.out.print("Choose the number of the question you wish to change: ");
                question = sc.nextInt();
                if ((question > 0) && (question < questionPool.getQuestions().length)) {
                    break;
                }
                System.out.println("Incorrect input, try again...");
            }
            sc.nextLine();
            System.out.print("Enter question content: ");
            questionPool.changeQuestionContent(question, sc.nextLine());
            System.out.println("Finished");
        } else {
            System.out.println("There are no questions to change, aborting...");
        }
    }

    private static void printAllQuestions(QuestionPool questionPool) {
        if (questionPool.hasQuestions()) { // checking if there are questions
            System.out.println(questionPool);
        } else {
            System.out.println("There are no questions, aborting...");
        }
    }

    private static void addAnswerToPool(Scanner sc, AnswerPool answerPool) {
        System.out.print("Enter answer here: ");
        String content = sc.nextLine();
        boolean bool;
        System.out.print("Is the answer correct?[true/false]: ");
        bool = sc.nextBoolean();
        sc.nextLine();
        answerPool.addAnswer(content, bool);
        System.out.println("Finished");
    }

    private static void addAnswerToQuestion(Scanner sc, QuestionPool questionPool, AnswerPool answerPool) {
        if ((answerPool.hasAnswers()) && (questionPool.hasSelectionQuestions())) { // checking if there are selection
            // questions/answers
            System.out.println(answerPool);
            int answer;
            while (true) { // validating input
                System.out.print("Choose the number of the answer you wish to add: ");
                answer = sc.nextInt();
                sc.nextLine();
                if ((answer > 0) && (answer < answerPool.getAnswers().length)) {
                    break;
                }
                System.out.println("Incorrect input, try again...");
            }
            System.out.println(questionPool.toStringSelection());
            int question;
            while (true) { // validating input
                System.out.print("Choose the number of the question you wish to add to: ");
                question = sc.nextInt();
                sc.nextLine();
                if ((question > 0) && (question < questionPool.getQuestions().length)
                        && (!(questionPool.getQuestion(question) instanceof OpenQuestion))) {
                    break;
                }
                System.out.println("Incorrect input, try again...");
            }
            questionPool.addAnswer(question, answerPool.getAnswer(answer));
            System.out.println("Finished");
        } else {
            System.out.println("There are no selection questions and/or answers, aborting...");
        }
    }

    private static void deleteAnswerFromPool(Scanner sc, AnswerPool answerPool) {
        if (answerPool.hasAnswers()) { // checking if there are answers
            System.out.println(answerPool);
            int answer;
            while (true) { // validating input
                System.out.print("Choose the number of the answer you wish to delete: ");
                answer = sc.nextInt();
                sc.nextLine();
                if ((answer > 0) && (answer < answerPool.getAnswers().length)) {
                    break;
                }
                System.out.println("Incorrect input, try again...");
            }
            answerPool.deleteAnswer(answer);
            System.out.println("Finished");
        } else {
            System.out.println("There are no answers to delete, aborting...");
        }
    }

    private static void deleteAnswerFromQuestion(Scanner sc, QuestionPool questionPool, AnswerPool answerPool) {
        if (questionPool.hasSelectionQuestions()) { // checking if there are selection questions
            System.out.println(questionPool.toStringSelection());
            int question;
            while (true) { // validating input
                System.out.print("Choose the number of the question you wish to delete from: ");
                question = sc.nextInt();
                sc.nextLine();
                if ((question > 0) && (question < questionPool.getQuestions().length)
                        && (!(questionPool.getQuestion(question) instanceof OpenQuestion))) {
                    break;
                }
                System.out.println("Incorrect input, try again...");
            }
            if ((questionPool.getQuestion(question).getAnswers()[0] != null)
                    && (!Objects.equals(questionPool.getQuestion(question).getAnswers()[0].getContent(), ""))) { // checking if the
                // selection
                // question has
                // answers
                System.out.println(questionPool.getQuestion(question).toString());
                int answer;
                while (true) { // validating input
                    System.out.print("Choose the number of the answer you wish to delete: ");
                    answer = sc.nextInt();
                    sc.nextLine();
                    if ((answer > 0) && (answer < answerPool.getAnswers().length)) {
                        break;
                    }
                    System.out.println("Incorrect input, try again...");
                }
                questionPool.deleteAnswer(question, answer);
                System.out.println("Finished");
            } else {
                System.out.println("There are no answers in that question, aborting...");
            }
        } else {
            System.out.println("There are no selection questions to delete from, aborting...");
        }
    }

    private static void changAnswerContent(Scanner sc, AnswerPool answerPool) {
        if (answerPool.hasAnswers()) { // checking if there are answers
            System.out.println(answerPool);
            int answer;
            while (true) { // validating input
                System.out.print("Choose the number of the answer you wish to change: ");
                answer = sc.nextInt();
                sc.nextLine();
                if ((answer > 0) && (answer < answerPool.getAnswers().length)) {
                    break;
                }
                System.out.println("Incorrect input, try again...");
            }
            System.out.print("Enter answer content: ");
            String content = sc.nextLine();
            System.out.print("Is the answer correct?[true/false]: ");
            answerPool.changeAnswerContent(answer, content, sc.nextBoolean());
            System.out.println("Finished");
        } else {
            System.out.println("There are no answers to change, aborting...");
        }
    }

    private static void changeOpenAnswer(Scanner sc, QuestionPool questionPool) {
        if (questionPool.hasOpenQuestions()) { // checking if there are open questions
            System.out.println(questionPool.toStringOpen());
            int question;
            String content;
            while (true) { // validating input
                System.out.print("Choose the number of the open question you wish to change its answer: ");
                question = sc.nextInt();
                sc.nextLine();
                if ((question > 0) && (question < questionPool.getQuestions().length)
                        && (questionPool.getQuestion(question) instanceof OpenQuestion)) {
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

    private static void printAnswers(AnswerPool answerPool) {
        if (answerPool.hasAnswers()) { // checking if there are answers
            System.out.println(answerPool);

        } else {
            System.out.println("There are no answers, aborting...");

        }
    }

    private static void makeManualExam(Scanner sc, QuestionPool qpool) {
        try {
            if (validForTest(qpool, false)) {
                QuestionPool questionPool = new QuestionPool();
                for (int q = 1; q < qpool.getQuestions().length; q++) {
                    if (qpool.getQuestion(q) instanceof OpenQuestion) {
                        questionPool.addQuestion(qpool.getQuestion(q).getContent(),
                                qpool.getQuestion(q).getDifficultyInt(), qpool.getQuestion(q).getAnswer().getContent());
                    } else {
                        if (qpool.getQuestion(q).getAnswerCount() > 3) {
                            questionPool.addQuestion(qpool.getQuestion(q).getContent(),
                                    qpool.getQuestion(q).getDifficultyInt());
                            for (int a = 1; a <= qpool.getQuestion(q).getAnswerCount(); a++) {
                                questionPool.addAnswer(questionPool.getQuestions().length - 1,
                                        new Answer(qpool.getQuestion(q).getAnswer(a).getContent(),
                                                qpool.getQuestion(q).getAnswer(a).getCorrect()));
                            }
                        }
                    }
                }
                sc.nextLine();
                int current = 0;
                int amount = 0;
                int questionChoice;
                QuestionPool tempPool = new QuestionPool();
                boolean[] questionMarker = new boolean[questionPool.getQuestions().length];
                System.out.println(questionPool);
                System.out.print("Choose the number of the questions to add seperated by spaces: ");
                String questionInput = sc.nextLine();
                Scanner questionSc = new Scanner(questionInput);
                while (questionSc.hasNextInt()) {
                    amount++;
                }
                if (amount < 10) {
                    throw new ExamAmountException();
                }
                questionSc = new Scanner(questionInput);
                while (questionSc.hasNextInt()) {
                    questionChoice = questionSc.nextInt();
                    if ((questionChoice > 0) && (questionChoice < questionPool.getQuestions().length)) {
                        if (!questionMarker[questionChoice - 1]) {
                            current++;
                            if (!(questionPool.getQuestion(questionChoice) instanceof OpenQuestion)) {
                                tempPool.addQuestion(questionPool.getQuestion(questionChoice).getContent(),
                                        questionPool.getQuestion(questionChoice).getDifficultyInt());

                                if (questionPool.getQuestion(questionChoice).getAnswerCount() < 4) {
                                    throw new ExamSelectionException();
                                }
                                boolean[] answerMarker = new boolean[10];
                                System.out.println(questionPool.getQuestion(questionChoice).toString());
                                System.out.print("Choose the number of the answers to add seperated by spaces: ");
                                String answerInput = sc.nextLine();
                                Scanner answerSc = new Scanner(answerInput);
                                while (answerSc.hasNextInt()) {
                                    int answerChoice = answerSc.nextInt();
                                    if ((answerChoice > 0) && (answerChoice <= 10)) {
                                        if (!answerMarker[answerChoice - 1]) {
                                            System.out.println(questionPool.getQuestion(questionChoice)
                                                    .getAnswer(answerChoice).toString());
                                            tempPool.addAnswer(current,
                                                    questionPool.getQuestion(questionChoice).getAnswer(answerChoice));
                                            answerMarker[answerChoice - 1] = true;
                                        }
                                    }
                                }
                                answerSc.close();
                            } else {
                                tempPool.addQuestion(questionPool.getQuestion(questionChoice).getContent(),
                                        questionPool.getQuestion(questionChoice).getDifficultyInt(),
                                        questionPool.getQuestion(questionChoice).getAnswer().getContent());
                            }
                            questionMarker[questionChoice - 1] = true;
                        }
                    }
                }
                questionSc.close();
                ManualExam exam = new ManualExam(tempPool);
                exam.createExam();
            }
        } catch (ExamException e) {
            System.out.println(e.getMessage());
        }
    }

    private static void makeAutomaticExam(Scanner sc, QuestionPool qpool) {
        try {
            if (validForTest(qpool, true)) {
                QuestionPool questionPool = new QuestionPool();
                for (int q = 1; q < qpool.getQuestions().length; q++) {
                    if (qpool.getQuestion(q) instanceof OpenQuestion) {
                        questionPool.addQuestion(qpool.getQuestion(q).getContent(),
                                qpool.getQuestion(q).getDifficultyInt(), qpool.getQuestion(q).getAnswer().getContent());
                    } else {
                        if ((qpool.getQuestion(q).getAnswerCount() - qpool.getQuestion(q).getNumOfTrues() >= 3)
                                && (qpool.getQuestion(q).getAnswerCount() > 3)) {
                            questionPool.addQuestion(qpool.getQuestion(q).getContent(),
                                    qpool.getQuestion(q).getDifficultyInt());
                            for (int a = 1; a <= qpool.getQuestion(q).getAnswerCount(); a++) {
                                questionPool.addAnswer(questionPool.getQuestions().length - 1,
                                        new Answer(qpool.getQuestion(q).getAnswer(a).getContent(),
                                                qpool.getQuestion(q).getAnswer(a).getCorrect()));
                            }
                        }
                    }
                }
                QuestionPool tempPool = new QuestionPool();
                System.out.print("enter amount of questions to add: ");
                int amount = sc.nextInt();
                if (amount > validForTestCount(questionPool)) {
                    throw new ExamException("Too few questions");
                }
                if (amount < 10) {
                    throw new ExamAmountException();
                }
                int count = 0;
                Random rnd = new Random();
                boolean trueSelected = false;
                do {
                    int questionSelection = rnd.nextInt(1, questionPool.getQuestions().length);
                    if (questionPool.getQuestion(questionSelection) instanceof OpenQuestion) {
                        tempPool.addQuestion(questionPool.getQuestion(questionSelection).getContent(),
                                questionPool.getQuestion(questionSelection).getDifficultyInt(),
                                questionPool.getQuestion(questionSelection).getAnswer().getContent());
                    } else {
                        tempPool.addQuestion(questionPool.getQuestion(questionSelection).getContent(),
                                questionPool.getQuestion(questionSelection).getDifficultyInt());
                        while (true) {
                            int answerSelection = rnd.nextInt(1,
                                    (questionPool.getQuestion(questionSelection).getAnswerCount() + 1));
                            if ((!trueSelected) && (questionPool.getQuestion(questionSelection)
                                    .getAnswer(answerSelection).getCorrect())) {
                                tempPool.addAnswer(tempPool.getQuestions().length - 1,
                                        questionPool.getQuestion(questionSelection).getAnswer(answerSelection));
                                questionPool.deleteAnswer(questionSelection, answerSelection);
                                trueSelected = true;
                            } else {
                                if (!questionPool.getQuestion(questionSelection).getAnswer(answerSelection)
                                        .getCorrect()) {
                                    tempPool.addAnswer(tempPool.getQuestions().length - 1,
                                            questionPool.getQuestion(questionSelection).getAnswer(answerSelection));
                                    questionPool.deleteAnswer(questionSelection, answerSelection);
                                }
                            }
                            if (tempPool.getQuestion(tempPool.getQuestions().length - 1).getAnswerCount() > 3) {
                                trueSelected = false;
                                break;
                            }
                        }
                    }
                    questionPool.deleteQuestion(questionSelection);
                    count++;
                } while (count < amount);
                AutomaticExam exam = new AutomaticExam(tempPool);
                exam.createExam();
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private static void printAll(QuestionPool questionPool) {
        if (questionPool.hasQuestions()) { // checking if there are questions
            System.out.println(questionPool.toStringFull());

        } else {
            System.out.println("There are no questions, aborting...");

        }
    }

    private static Boolean validForTest(QuestionPool questionPool, boolean automatic) throws ExamQuestionsException {
        int size = questionPool.getQuestions().length;
        for (int i = 1; i < size; i++) {
            if ((!(questionPool.getQuestion(i) instanceof OpenQuestion))
                    && (questionPool.getQuestion(i).getAnswerCount() < 4)) {
                size--;
            } else {
                if ((automatic) && (questionPool.getQuestion(i).getAnswerCount()
                        - questionPool.getQuestion(i).getNumOfTrues() < 3)) {
                    size--;
                }
            }
        }
        if (size < 10) {
            throw new ExamQuestionsException();
        }
        return true;
    }

    private static int validForTestCount(QuestionPool questionPool) {
        int size = questionPool.getQuestions().length;
        for (int i = 1; i < size; i++) {
            if ((!(questionPool.getQuestion(i) instanceof OpenQuestion))
                    && (questionPool.getQuestion(i).getAnswerCount() < 4)) {
                size--;
            } else {
                if ((questionPool.getQuestion(i).getAnswerCount()
                        - questionPool.getQuestion(i).getNumOfTrues() < 3)) {
                    size--;
                }
            }
        }
        return size;
    }
}

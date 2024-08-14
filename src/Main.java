import java.sql.*;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;
import java.util.Random;

public class Main {

    private static Statement stmt;
    private static Scanner sc;
    private static int SID;
    private static int TID;
    private static final int minExamQuestions = 4;
    private static final int maxSelections = 10;
    private static TeacherPool teacherPool;
    private static SubjectPool subjectPool;
    private static QuestionPool questionPool;
    private static AnswerPool answerPool;
    private static Connection con;
    private static Random random;

    public static void main(String[] args) throws SQLException {

        // Connecting to database
        try {
            /////////////// CHANGE THIS FOR TESTING ///////////////
            con = DriverManager.getConnection("jdbc:postgresql://localhost:5432/examdb", "postgres", "admin");
            System.out.println("Connected to database!");
            stmt = con.createStatement();

            // initializing
            subjectPool = new SubjectPool(con);
            questionPool = new QuestionPool(con);
            answerPool = new AnswerPool(con);
            teacherPool = new TeacherPool(con);
            random = new Random();

        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }

        SID = 0;
        TID = 0;
        sc = new Scanner(System.in);
        int choice;
        boolean exit = false;
        wakeup();
        initiateUser();


        System.out.println("Welcome to my test GUI! \n");
        while (true) {
            try {
                System.out.println("Current subject: " + subjectPool.getSubjectName(SID));
                System.out.println(
                        "1: question related.\n2: answer related.\n3: exam related.\n4: subject related.\n5: print all questions with answers.\n6: Switch user (teacher).\n0: exit.");
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
                    case 3: { // Exam sub-menu
                        while (true) {
                            System.out.println(
                                    "1: create manual exam.\n2: create automatic exam.\n3: print exam.\n4: print exam answers.\n0: go back.");
                            choice = sc.nextInt();
                            switch (choice) {
                                case 1: { // create manual exam
                                    CreateManualExam();
                                    exit = true;
                                    break;
                                }
                                case 2: { // create automatic exam
                                    CreateAutomaticExam();
                                    exit = true;
                                    break;
                                }
                                case 3: { // print exam
                                    printExam(false);
                                    exit = true;
                                    break;
                                }
                                case 4: { // print exam answers
                                    printExam(true);
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
                    case 6: { // Switch user
                        initiateUser();
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

    private static void CreateAutomaticExam() throws SQLException {
        String query = "SELECT COUNT(*) as total FROM question WHERE sid = " + SID;
        ResultSet resultSet = stmt.executeQuery(query);
        resultSet.next();
        if (resultSet.getInt("total") < minExamQuestions) {
            System.out.println("You need at least " + minExamQuestions + " questions in the database!");
        }
        else{
            ArrayList<Integer> list = new ArrayList<>();
            query = "SELECT qid FROM question WHERE sid = " + SID;
            resultSet = stmt.executeQuery(query);
            while (resultSet.next()) {
                list.add(resultSet.getInt("qid"));
            }
            query = "INSERT INTO exam (sid, creation_time, tid) VALUES (" + SID + ", NOW(), " + TID + ")";
            stmt.executeUpdate(query);
            query = "SELECT * FROM exam WHERE sid = " + SID + " AND tid = " + TID + " ORDER BY creation_time DESC";
            resultSet = stmt.executeQuery(query);
            resultSet.next();
            int EID = resultSet.getInt("eid");
            int choice;
            System.out.print("Select exam size, from 4 to "+list.size()+": ");
            while (true){
                choice = sc.nextInt();
                if (choice >=4 && choice <=list.size()){
                    break;
                }
                System.out.println("Incorrect input, try again.");
            }
            int rand;
            for (int i = 0; i < choice; i++) {
                rand = random.nextInt(list.size());
                query = "INSERT INTO exam_question (eid, qid) VALUES (" + EID + ", " + list.get(rand) + ")";
                stmt.executeUpdate(query);
                list.remove(rand);
            }
        }
    }

    private static void CreateManualExam() throws SQLException {
        if (questionPool.getQuestionsLen() < 1) {
            System.out.println("There are no question on the subject!");
            return;
        }
        String query = "INSERT INTO exam (sid, creation_time, tid) VALUES (" + SID + ", NOW(), " + TID + ")";
        stmt.executeUpdate(query);
        query = "SELECT * FROM exam WHERE sid = " + SID + " AND tid = " + TID + " ORDER BY creation_time DESC";
        ResultSet resultSet = stmt.executeQuery(query);
        resultSet.next();
        int EID = resultSet.getInt("eid");
        System.out.println("Exam created!");
        int choice;
        while (true) {
            System.out.println(questionPool.toString());
            System.out.print("Exam contains [ ");
            query = "SELECT qid FROM exam_question WHERE eid = " + EID + " ORDER BY qid ASC";
            resultSet = stmt.executeQuery(query);
            while (resultSet.next()) {
                System.out.print(resultSet.getString("qid") + " ");
            }
            System.out.print("]\nSelect question id to add to the test, 0 to finish: ");
            choice = sc.nextInt();
            if (choice == 0) {
                break;
            }
            query = "SELECT COUNT(*) as total FROM question WHERE sid = " + SID + " AND qid = " + choice;
            resultSet = stmt.executeQuery(query);
            resultSet.next();
            if (resultSet.getInt("total") > 0) {
                query = "SELECT * FROM exam_question WHERE eid = " + EID + " AND qid = " + choice;
                resultSet = stmt.executeQuery(query);
                if (resultSet.next()) {
                    System.out.println("Question is already added!");
                } else {
                    query = "INSERT INTO exam_question (eid, qid) VALUES (" + EID + ", " + choice + ")";
                    stmt.executeUpdate(query);
                    System.out.println("Question added!");
                }
            } else {
                System.out.println("Question does not exist!");
            }
        }
    }

    private static void printExam(Boolean full) throws SQLException {
        String query = "SELECT COUNT(*) as total FROM exam WHERE sid = " + SID + " AND tid = " + TID;
        ResultSet resultSet = stmt.executeQuery(query);
        resultSet.next();
        if (resultSet.getInt("total") < 1) {
            System.out.println("There are no exams on the subject!");
            return;
        }
        query = "SELECT eid, subject.name, teacher.name, creation_time FROM subject JOIN exam ON subject.sid = exam.sid JOIN teacher ON exam.tid = teacher.tid WHERE exam.sid = " + SID + " AND exam.tid = " + TID;
        resultSet = stmt.executeQuery(query);
        while (resultSet.next()) {
            System.out.println(resultSet.getInt(1) + ") " + resultSet.getString(2) + " test by " + resultSet.getString(3) + " on " + resultSet.getString(4) + ".");
        }
        int choice;
        while (true) {
            System.out.print("Choose exam ID: ");
            choice = sc.nextInt();
            query = "SELECT subject.name, teacher.name, creation_time FROM subject JOIN exam ON subject.sid = exam.sid JOIN teacher ON exam.tid = teacher.tid WHERE exam.eid = " + choice;
            resultSet = stmt.executeQuery(query);
            if (resultSet.next()) {
                System.out.println("------------------------------\n" + resultSet.getString(1) + " test by " + resultSet.getString(2) + " on " + resultSet.getString(3) + ".\n\n");
                query = "SELECT * FROM exam_question WHERE eid = " + choice;
                resultSet = stmt.executeQuery(query);
                while (resultSet.next()) {
                    if(full){
                        System.out.println(questionPool.toStringSpecificFull(resultSet.getInt("qid")));
                    }
                    else{
                        System.out.println(questionPool.toStringSpecific(resultSet.getInt("qid")));
                    }
                }
                System.out.println("------------------------------");
                return;
            } else {
                System.out.println("Exam does not exist!");
            }
        }
    }

    private static void initiateUser() throws SQLException {

        int choice;

        //initial teacher selection
        if (teacherPool.getTeachersLen() == 0) {
            System.out.println("There are no teachers, please enter a new one");
            TID = createTeacher();
        } else {
            System.out.println("1: choose existing teacher.\n2: create new teacher");
            choice = sc.nextInt();
            switch (choice) { // menu
                case 1: { // choosing existing subject
                    sc.nextLine();
                    TID = changeTeacher();
                    break;
                }
                case 2: { // creating new subject
                    sc.nextLine();
                    TID = createTeacher();
                    break;
                }
                default: { // validating input
                    System.out.println("Incorrect input, try again.");
                    sc.nextLine();
                    break;
                }
            }
        }

        // initial subject selection
        if (subjectPool.getSubjectsLen() == 0) {
            System.out.println("There are no subjects, please create a new one");
            sc = new Scanner(System.in);
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
    }

    private static int createTeacher() throws SQLException {
        System.out.print("Enter teacher full name: ");
        String name = sc.nextLine();
        System.out.print("Enter teacher address: ");
        String address = sc.nextLine();
        System.out.print("Enter teacher years of experience: ");
        int exp;
        while (true) {
            if (sc.hasNextInt()) {
                exp = sc.nextInt();
                break;
            } else {
                System.out.println("Incorrect input, try again.");
            }
        }


        sc.reset();
        return teacherPool.addTeacher(name, address, exp);
    }

    private static int changeTeacher() throws SQLException {
        System.out.println(teacherPool.toString());
        System.out.print("Enter teacher number: ");
        while (true) {
            int choice = sc.nextInt();
            String query = "SELECT * FROM teacher WHERE tid = " + choice;
            ResultSet resultSet = stmt.executeQuery(query);
            if (resultSet.next()) {
                sc.reset();
                return choice;
            }
            System.out.println("Wrong input, try again");
        }
    }

    private static void changeSubjectName() throws SQLException {
        System.out.println("Current name: " + subjectPool.getSubjectName(SID));
        System.out.print("Enter new name: ");
        subjectPool.setSubjectName(SID, sc.nextLine());
    }

    private static int changeSubject() throws SQLException {
        System.out.println(subjectPool.toString(TID, con));
        System.out.print("Enter subject number: ");
        while (true) {
            int choice = sc.nextInt();
            String query = "SELECT * FROM subject JOIN subject_teacher ON subject.sid = subject_teacher.sid WHERE subject.sid = " + choice + " AND subject_teacher.tid = " + TID;
            ResultSet resultSet = stmt.executeQuery(query);
            if (resultSet.next()) {
                return choice;
            }
            else{
                query = "SELECT * FROM subject  WHERE sid = " + choice;
                resultSet = stmt.executeQuery(query);
                if (resultSet.next()) {
                    subjectPool.enrollSubject(choice, TID);
                    return choice;
                }
            }
            System.out.println("Wrong input, try again");
        }
    }

    private static int createSubject() throws SQLException {
        System.out.print("Enter subject name: ");
        String name = sc.nextLine();
        return subjectPool.addSubject(name, TID);
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
            questionPool.addAnswer(question, answer, bool, maxSelections);
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
                String query = "SELECT * FROM open_question WHERE qid = " + question;
                ResultSet resultSet = stmt.executeQuery(query);
                if ((resultSet.next()) && (questionPool.isOpen(question))) {
                    System.out.print("Enter answer content: ");
                    content = sc.nextLine();
                    break;
                }
                System.out.println("Incorrect input, try again...");
            }
            questionPool.setAnswer(content, question, answerPool);
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
            System.out.println(questionPool.toStringFull(con));

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

    private static void wakeup() throws SQLException {
        String query = "SELECT COUNT(table_name) AS total FROM information_schema.tables WHERE table_schema = 'public'";
        ResultSet resultSet = stmt.executeQuery(query);
        resultSet.next();
        if(resultSet.getInt("total") != 9){
            System.out.println("Issue detected, recreating database...");
            query = """
                    DO $$ DECLARE
                        r RECORD;
                    BEGIN
                        FOR r IN (SELECT tablename FROM pg_tables WHERE schemaname = 'public') LOOP
                            EXECUTE 'DROP TABLE IF EXISTS ' || quote_ident(r.tablename) || ' CASCADE';
                        END LOOP;
                    END $$;""";
            stmt.executeUpdate(query);
            query = """
                    CREATE TABLE subject
                      (
                          sid  SERIAL PRIMARY KEY,
                          name TEXT NOT NULL
                      );
                    
                      CREATE TABLE teacher
                      (
                          tid          SERIAL PRIMARY KEY,
                          name         TEXT NOT NULL,
                          address      TEXT NOT NULL,
                          years_of_exp INTEGER
                      );
                    
                      CREATE TABLE subject_teacher
                      (
                          sid INTEGER NOT NULL REFERENCES subject (sid),
                          tid INTEGER NOT NULL REFERENCES teacher (tid),
                          PRIMARY KEY (sid, tid)
                      );
                    
                      CREATE TABLE question
                      (
                          qid           SERIAL PRIMARY KEY,
                          sid           INTEGER REFERENCES subject (sid),
                          difficulty    INTEGER CHECK (difficulty BETWEEN 1 AND 3),
                          question_text TEXT NOT NULL,
                          is_selection  BOOLEAN
                      );
                    
                      CREATE TABLE answer
                      (
                          aid         SERIAL PRIMARY KEY,
                          sid         INTEGER REFERENCES subject (sid),
                          answer_text TEXT NOT NULL
                      );
                    
                      CREATE TABLE open_question
                      (
                          qid SERIAL PRIMARY KEY,
                          aid INTEGER REFERENCES answer (aid),
                          FOREIGN KEY (qid) REFERENCES question (qid) ON DELETE CASCADE
                      );
                    
                      CREATE TABLE selection_question
                      (
                          qid        INTEGER,
                          aid        INTEGER,
                          is_correct BOOLEAN,
                          PRIMARY KEY (qid, aid),
                          FOREIGN KEY (qid) REFERENCES question(qid) ON DELETE CASCADE,
                          FOREIGN KEY (aid) REFERENCES answer(aid)
                      );
                    
                      CREATE TABLE exam
                      (
                          eid           SERIAL PRIMARY KEY,
                          sid           INTEGER REFERENCES subject (sid),
                          creation_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                          tid           INTEGER REFERENCES teacher (tid)
                      );
                    
                      CREATE TABLE exam_question
                      (
                          eid INTEGER REFERENCES exam (eid),
                          qid INTEGER REFERENCES question,
                          PRIMARY KEY (eid, qid)
                      );
                    """;
            stmt.executeUpdate(query);
        }
    }
}

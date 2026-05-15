import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.layout.Priority;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.extractor.XWPFWordExtractor;

import java.io.File;
import java.nio.file.Files;

public class MainUI extends Application {

    private TextArea resumeInput;

    private TextFlow resumeFlow;

    private ProgressBar progressBar;

    private Label scoreLabel;
    private Label matchLabel;

    private ComboBox<String> roleBox;

    // ANALYSIS SECTION

    private VBox matchedBox;
    private VBox missingBox;
    private VBox suggestionBox;

    private Label roleResult;
    private Label atsResult;

    @Override
    public void start(Stage stage) {

        // =====================================================
        // HEADER
        // =====================================================

        Label heading =
                new Label(
                        "ATS-Insight: Smart Resume Analyzer"
                );

        heading.getStyleClass().add(
                "main-heading"
        );

        Label subHeading =
                new Label(
                        "Analyze your resume for ATS score, matched skills and suggestions."
                );

        subHeading.getStyleClass().add(
                "sub-heading"
        );

        VBox headerBox =
                new VBox(5, heading, subHeading);

        // =====================================================
        // BUTTONS
        // =====================================================

        Button uploadBtn =
                new Button(" Upload Resume");

        Button analyzeBtn =
                new Button(" Analyze Resume");

        // Upload Icon

        ImageView uploadIcon =
                new ImageView(
                        new Image(
                                getClass().getResourceAsStream("/icons/upload.png")
                        )
                );

        uploadIcon.setFitWidth(18);
        uploadIcon.setFitHeight(18);

        uploadBtn.setGraphic(uploadIcon);

        // Analyze Icon

        ImageView analyzeIcon =
                new ImageView(
                        new Image(
                                getClass().getResourceAsStream("/icons/analyze.png")
                        )
                );

        analyzeIcon.setFitWidth(18);
        analyzeIcon.setFitHeight(18);

        analyzeBtn.setGraphic(analyzeIcon);

        HBox buttonBox =
                new HBox(15, uploadBtn, analyzeBtn);

        // =====================================================
        // ROLE BOX
        // =====================================================

        roleBox = new ComboBox<>();

        roleBox.getItems().addAll(

                "Backend Developer",
                "Frontend Developer",
                "Full Stack Developer",
                "Data Analyst",
                "Data Scientist",
                "Machine Learning Engineer",
                "DevOps Engineer",
                "Cloud Engineer",
                "Cybersecurity Analyst",
                "Android Developer"
        );

        roleBox.setValue("Full Stack Developer");

        roleBox.setPrefWidth(300);

        // =====================================================
        // RESUME INPUT
        // =====================================================

        resumeInput = new TextArea();

        resumeInput.setPromptText(
                "Paste or upload resume here..."
        );

        resumeInput.setPrefHeight(130);

        // =====================================================
        // HIGHLIGHTED RESUME
        // =====================================================

        resumeFlow = new TextFlow();

        ScrollPane scrollPane =
                new ScrollPane(resumeFlow);

        scrollPane.setFitToWidth(true);

        scrollPane.setPrefHeight(70);

        // =====================================================
        // PROGRESS BAR
        // =====================================================

        progressBar = new ProgressBar(0);

        progressBar.setPrefWidth(600);

        progressBar.setPrefHeight(22);

        progressBar.getStyleClass().add(
                "custom-progress"
        );

        scoreLabel =
                new Label("Score: 0%");

        scoreLabel.getStyleClass().add(
                "score-label"
        );

        matchLabel =
                new Label("Waiting Analysis");

        matchLabel.getStyleClass().add(
                "match-label"
        );

        VBox scoreCard =
                new VBox(8);

        scoreCard.setAlignment(Pos.CENTER);

        scoreCard.getStyleClass().add(
                "score-card"
        );

        scoreCard.getChildren().addAll(
                scoreLabel,
                matchLabel
        );

        VBox progressSection =
                new VBox(
                        10,
                        new Label("ATS Score:"),
                        progressBar
                );

        HBox scoreSection =
                new HBox(
                        25,
                        progressSection,
                        scoreCard
                );

        scoreSection.setAlignment(Pos.CENTER_LEFT);

        // =====================================================
        // ANALYSIS DASHBOARD
        // =====================================================

        Label analysisTitle =
                new Label("Analysis Result:");

        analysisTitle.getStyleClass().add(
                "analysis-title"
        );

        roleResult =
                new Label("Selected Role: -");

        roleResult.getStyleClass().add(
                "result-top"
        );

        atsResult =
                new Label("ATS Score: 0%");

        atsResult.getStyleClass().add(
                "result-top"
        );

        // MATCHED BOX

        matchedBox = new VBox(10);

        matchedBox.getStyleClass().add(
                "analysis-card"
        );

        Label matchedTitle =
                new Label("✔ Matched Skills");

        matchedTitle.getStyleClass().add(
                "matched-title"
        );

        matchedBox.getChildren().add(
                matchedTitle
        );

        // MISSING BOX

        missingBox = new VBox(10);

        missingBox.getStyleClass().add(
                "analysis-card"
        );

        Label missingTitle =
                new Label("✖ Missing Skills");

        missingTitle.getStyleClass().add(
                "missing-title"
        );

        missingBox.getChildren().add(
                missingTitle
        );

        // SUGGESTION BOX

        suggestionBox = new VBox(10);

        suggestionBox.getStyleClass().add(
                "analysis-card"
        );

        Label suggestionTitle =
                new Label("✦ Suggestions");

        suggestionTitle.getStyleClass().add(
                "suggestion-title"
        );

        suggestionBox.getChildren().add(
                suggestionTitle
        );

        // =====================================================
        // CARD SCROLLS
        // =====================================================

        ScrollPane matchedScroll =
                new ScrollPane(matchedBox);

        matchedScroll.setFitToWidth(true);

        matchedScroll.setPrefHeight(240);

        ScrollPane missingScroll =
                new ScrollPane(missingBox);

        missingScroll.setFitToWidth(true);

        missingScroll.setPrefHeight(240);

        ScrollPane suggestionScroll =
                new ScrollPane(suggestionBox);

        suggestionScroll.setFitToWidth(true);

        suggestionScroll.setPrefHeight(240);

        // =====================================================
        // ANALYSIS GRID
        // =====================================================

        HBox analysisGrid =
                new HBox(
                        18,
                        matchedScroll,
                        missingScroll,
                        suggestionScroll
                );

        analysisGrid.setAlignment(Pos.CENTER);

        HBox.setHgrow(
                matchedScroll,
                Priority.ALWAYS
        );

        HBox.setHgrow(
                missingScroll,
                Priority.ALWAYS
        );

        HBox.setHgrow(
                suggestionScroll,
                Priority.ALWAYS
        );

        matchedScroll.setMaxWidth(Double.MAX_VALUE);
        missingScroll.setMaxWidth(Double.MAX_VALUE);
        suggestionScroll.setMaxWidth(Double.MAX_VALUE);

        analysisGrid.setAlignment(Pos.CENTER);

        VBox analysisSection =
                new VBox(
                        15,
                        analysisTitle,
                        roleResult,
                        atsResult,
                        analysisGrid
                );

        analysisSection.getStyleClass().add(
                "analysis-section"
        );

        // =====================================================
        // MAIN LAYOUT
        // =====================================================

        VBox mainLayout =
                new VBox(18);

        mainLayout.setPadding(
                new Insets(22)
        );

        mainLayout.getStyleClass().add(
                "main-layout"
        );

        // =====================================================
        // RESUME SECTION SIDE BY SIDE
        // =====================================================

        Label resumeLabel =
                new Label("Resume Input:");

        resumeLabel.getStyleClass().add(
                "section-label"
        );

        Label highlightLabel =
                new Label("Highlighted Resume:");

        highlightLabel.getStyleClass().add(
                "section-label"
        );

// LEFT SIDE

        VBox resumeBox =
                new VBox(
                        10,
                        resumeLabel,
                        resumeInput
                );
        resumeBox.setPrefWidth(500);

        VBox.setVgrow(
                resumeInput,
                Priority.ALWAYS
        );

// RIGHT SIDE

        VBox highlightBox =
                new VBox(
                        10,
                        highlightLabel,
                        scrollPane
                );

        highlightBox.setPrefWidth(500);

        VBox.setVgrow(
                scrollPane,
                Priority.ALWAYS
        );

        // MAIN HORIZONTAL SECTION

        HBox resumeSection =
                new HBox(
                        20,
                        resumeBox,
                        highlightBox
                );

        resumeSection.setAlignment(Pos.CENTER);

        HBox.setHgrow(
                resumeBox,
                Priority.ALWAYS
        );

        HBox.setHgrow(
                highlightBox,
                Priority.ALWAYS
        );

        // =====================================================
        // MAIN LAYOUT
        // =====================================================

        mainLayout.getChildren().addAll(

                headerBox,

                buttonBox,

                new Label("Select Job Role:"),
                roleBox,

                resumeSection,

                scoreSection,

                analysisSection
        );

        // =====================================================
        // SCROLL PANE
        // =====================================================

        ScrollPane mainScroll = new ScrollPane();

        mainScroll.setContent(mainLayout);

        mainScroll.setFitToWidth(true);

        mainScroll.setPannable(true);

        mainScroll.setHbarPolicy(
                ScrollPane.ScrollBarPolicy.NEVER
        );

        mainScroll.setVbarPolicy(
                ScrollPane.ScrollBarPolicy.AS_NEEDED
        );

        // =====================================================
        // SCENE
        // =====================================================

        Scene scene =
                new Scene(
                        mainScroll,
                        800,
                        720
                );

        scene.getStylesheets().add(
                getClass().getResource("/styles.css").toExternalForm()
        );

        // =====================================================
        // BUTTON ACTIONS
        // =====================================================

        uploadBtn.setOnAction(
                e -> handleUpload(stage)
        );

        analyzeBtn.setOnAction(
                e -> handleAnalyze()
        );

        // =====================================================
        // STAGE
        // =====================================================

        stage.setTitle("ATS-Insight");

        stage.setScene(scene);

        stage.show();
    }

    // =====================================================
    // FILE UPLOAD
    // =====================================================

    private void handleUpload(Stage stage) {

        FileChooser fileChooser =
                new FileChooser();

        fileChooser.getExtensionFilters().addAll(

                new FileChooser.ExtensionFilter(
                        "Supported Files",
                        "*.txt",
                        "*.pdf",
                        "*.docx"
                )
        );

        File file =
                fileChooser.showOpenDialog(stage);

        if (file != null) {

            try {

                String content = "";

                // TXT

                if (file.getName().endsWith(".txt")) {

                    content =
                            Files.readString(
                                    file.toPath()
                            );
                }

                // PDF

                else if (file.getName().endsWith(".pdf")) {

                    PDDocument document =
                            PDDocument.load(file);

                    PDFTextStripper stripper =
                            new PDFTextStripper();

                    content =
                            stripper.getText(document);

                    document.close();
                }

                // DOCX

                else if (file.getName().endsWith(".docx")) {

                    XWPFDocument doc =
                            new XWPFDocument(
                                    Files.newInputStream(
                                            file.toPath()
                                    )
                            );

                    XWPFWordExtractor extractor =
                            new XWPFWordExtractor(doc);

                    content =
                            extractor.getText();

                    doc.close();
                }

                resumeInput.setText(content);

            }

            catch (Exception ex) {

                ex.printStackTrace();
            }
        }
    }

    // =====================================================
    // ANALYZE
    // =====================================================

    private void handleAnalyze() {

        String originalText =
                resumeInput.getText();

        String resumeText =
                originalText.toLowerCase();

        if (resumeText.isEmpty()) {

            return;
        }

        // =====================================================
        // ROLE KEYWORDS
        // =====================================================

        String selectedRole =
                roleBox.getValue();

        String[] keywords;

        switch (selectedRole) {

            case "Frontend Developer":

                keywords = new String[]{
                        "html",
                        "css",
                        "javascript",
                        "react",
                        "angular",
                        "bootstrap"
                };

                break;

            case "Full Stack Developer":

                keywords = new String[]{
                        "html",
                        "css",
                        "javascript",
                        "react",
                        "node",
                        "java",
                        "sql",
                        "api",
                        "spring",
                        "hibernate",
                        "microservices"
                };
                break;

            case "Data Analyst":

                keywords = new String[]{
                        "python",
                        "sql",
                        "excel",
                        "power bi",
                        "tableau"
                };

                break;

            case "Data Scientist":

                keywords = new String[]{
                        "python",
                        "machine learning",
                        "deep learning",
                        "numpy",
                        "pandas"
                };

                break;

            case "Machine Learning Engineer":

                keywords = new String[]{
                        "tensorflow",
                        "pytorch",
                        "machine learning",
                        "python"
                };

                break;

            case "DevOps Engineer":

                keywords = new String[]{
                        "docker",
                        "kubernetes",
                        "jenkins",
                        "aws",
                        "linux"
                };

                break;

            case "Cloud Engineer":

                keywords = new String[]{
                        "aws",
                        "azure",
                        "gcp",
                        "cloud"
                };

                break;

            case "Cybersecurity Analyst":

                keywords = new String[]{
                        "cybersecurity",
                        "firewall",
                        "ethical hacking",
                        "encryption"
                };

                break;

            case "Android Developer":

                keywords = new String[]{
                        "java",
                        "kotlin",
                        "android",
                        "firebase"
                };

                break;

            default:

                keywords = new String[]{
                        "java",
                        "spring",
                        "hibernate",
                        "api",
                        "sql"
                };
        }

        // =====================================================
        // MATCH CHECK
        // =====================================================

        int matchCount = 0;

        StringBuilder missing =
                new StringBuilder();

        for (String skill : keywords) {

            if (resumeText.contains(skill)) {

                matchCount++;
            } else {

                missing.append(skill)
                        .append(" ");
            }
        }

        // =====================================================
        // SCORE
        // =====================================================

        int score =
                (int) (
                        (matchCount /
                                (double) keywords.length) * 100
                );

        progressBar.setProgress(
                score / 100.0
        );

        progressBar.getStyleClass().removeAll(
                "low",
                "medium",
                "high"
        );

        if (score < 40) {

            progressBar.getStyleClass().add(
                    "low"
            );

            scoreLabel.setStyle(
                    "-fx-text-fill: #ef4444;"
            );

            matchLabel.setText(
                    "Poor Match"
            );
        } else if (score < 70) {

            progressBar.getStyleClass().add(
                    "medium"
            );

            scoreLabel.setStyle(
                    "-fx-text-fill: #f59e0b;"
            );

            matchLabel.setText(
                    "Average Match"
            );
        } else {

            progressBar.getStyleClass().add(
                    "high"
            );

            scoreLabel.setStyle(
                    "-fx-text-fill: #22c55e;"
            );

            matchLabel.setText(
                    "Good Match"
            );
        }

        scoreLabel.setText(
                "Score: " + score + "%"
        );

        // =====================================================
        // HIGHLIGHT TEXT
        // =====================================================

        highlightText(
                originalText,
                keywords
        );

        // =====================================================
        // UPDATE TOP
        // =====================================================

        roleResult.setText(
                "👤 Selected Role: " + selectedRole
        );

        atsResult.setText(
                "⚙ ATS Score: " + score + "%"
        );

        // =====================================================
        // CLEAR OLD
        // =====================================================

        matchedBox.getChildren().clear();
        missingBox.getChildren().clear();
        suggestionBox.getChildren().clear();

        // =====================================================
        // TITLES
        // =====================================================

        Label matchedTitle =
                new Label(
                        "✔ Matched Skills (" +
                                matchCount + ")"
                );

        matchedTitle.getStyleClass().add(
                "matched-title"
        );

        matchedBox.getChildren().add(
                matchedTitle
        );

        Label missingTitle =
                new Label(
                        "✖ Missing Skills (" +
                                (keywords.length - matchCount)
                                + ")"
                );

        missingTitle.getStyleClass().add(
                "missing-title"
        );

        missingBox.getChildren().add(
                missingTitle
        );

        Label suggestionTitle =
                new Label(
                        "✦ Suggestions"
                );

        suggestionTitle.getStyleClass().add(
                "suggestion-title"
        );

        suggestionBox.getChildren().add(
                suggestionTitle
        );

        // =====================================================
        // ADD SKILLS
        // =====================================================

        for (String skill : keywords) {

            if (resumeText.contains(skill)) {

                Label skillLabel =
                        new Label("✔ " + skill);

                skillLabel.getStyleClass().add(
                        "matched-skill"
                );

                matchedBox.getChildren().add(
                        skillLabel
                );
            } else {

                Label missLabel =
                        new Label("✖ " + skill);

                missLabel.getStyleClass().add(
                        "missing-skill"
                );

                missLabel.setWrapText(true);

                missingBox.getChildren().add(
                        missLabel
                );
            }
        }

        // =====================================================
        // AI SUGGESTIONS
        // =====================================================

        // COMMON SUGGESTIONS

        addSuggestion(
                "Use ATS-friendly resume formatting."
        );

        addSuggestion(
                "Add key achievements in projects."
        );

        addSuggestion(
                "Include internships, certifications and hackathons."
        );

        addSuggestion(
                "Keep resume concise and role-focused."
        );

// =====================================================
// ROLE-BASED SUGGESTIONS
// =====================================================

        switch (selectedRole) {

            // =================================================
            // FRONTEND
            // =================================================

            case "Frontend Developer":

                addSuggestion(
                        "Create responsive UI projects using React or Angular."
                );

                addSuggestion(
                        "Improve JavaScript and frontend framework knowledge."
                );

                addSuggestion(
                        "Build attractive portfolio websites."
                );

                if (missing.toString().contains("react")) {

                    addSuggestion(
                            "Learn React.js and component-based architecture."
                    );
                }

                if (missing.toString().contains("javascript")) {

                    addSuggestion(
                            "Strengthen JavaScript concepts and ES6 features."
                    );
                }

                break;

            // =================================================
            // FULL STACK
            // =================================================

            case "Full Stack Developer":

                addSuggestion(
                        "Build full-stack projects and showcase them."
                );

                addSuggestion(
                        "Highlight project architectures and technologies used."
                );

                if (missing.toString().contains("python")) {

                    addSuggestion(
                            "Improve your Python programming skills."
                    );
                }

                if (missing.toString().contains("spring")) {

                    addSuggestion(
                            "Add Spring framework experience in your resume."
                    );
                }

                if (missing.toString().contains("hibernate")) {

                    addSuggestion(
                            "Include Hibernate ORM knowledge."
                    );
                }

                if (missing.toString().contains("microservices")) {

                    addSuggestion(
                            "Learn microservices architecture and REST APIs."
                    );
                }

                break;

            // =================================================
            // BACKEND
            // =================================================

            case "Backend Developer":

                addSuggestion(
                        "Improve API development and backend architecture skills."
                );

                addSuggestion(
                        "Work on database optimization and authentication systems."
                );

                if (missing.toString().contains("java")) {

                    addSuggestion(
                            "Strengthen Java and OOP concepts."
                    );
                }

                if (missing.toString().contains("sql")) {

                    addSuggestion(
                            "Practice SQL queries and database normalization."
                    );
                }

                if (missing.toString().contains("spring")) {

                    addSuggestion(
                            "Learn Spring Boot for enterprise backend development."
                    );
                }

                break;

            // =================================================
            // DATA ANALYST
            // =================================================

            case "Data Analyst":

                addSuggestion(
                        "Create dashboard projects using Power BI or Tableau."
                );

                addSuggestion(
                        "Practice real-world business data analysis."
                );

                if (missing.toString().contains("excel")) {

                    addSuggestion(
                            "Improve Excel formulas and data visualization skills."
                    );
                }

                if (missing.toString().contains("sql")) {

                    addSuggestion(
                            "Practice SQL for analytics and reporting."
                    );
                }

                if (missing.toString().contains("tableau")) {

                    addSuggestion(
                            "Learn Tableau dashboard creation."
                    );
                }

                break;

            // =================================================
            // DATA SCIENTIST
            // =================================================

            case "Data Scientist":

                addSuggestion(
                        "Build machine learning projects with real datasets."
                );

                addSuggestion(
                        "Showcase predictive modeling and analytics projects."
                );

                if (missing.toString().contains("machine learning")) {

                    addSuggestion(
                            "Study supervised and unsupervised learning algorithms."
                    );
                }

                if (missing.toString().contains("pandas")) {

                    addSuggestion(
                            "Improve data cleaning and preprocessing using Pandas."
                    );
                }

                if (missing.toString().contains("numpy")) {

                    addSuggestion(
                            "Learn NumPy for scientific computing."
                    );
                }

                break;

            // =================================================
            // DEVOPS
            // =================================================

            case "DevOps Engineer":

                addSuggestion(
                        "Learn CI/CD pipelines and deployment automation."
                );

                addSuggestion(
                        "Practice Docker and Kubernetes orchestration."
                );

                addSuggestion(
                        "Gain cloud deployment experience."
                );

                break;

            // =================================================
            // CLOUD
            // =================================================

            case "Cloud Engineer":

                addSuggestion(
                        "Learn AWS, Azure or Google Cloud services."
                );

                addSuggestion(
                        "Practice cloud deployment and scaling."
                );

                addSuggestion(
                        "Build projects using cloud infrastructure."
                );

                break;

            // =================================================
            // CYBERSECURITY
            // =================================================

            case "Cybersecurity Analyst":

                addSuggestion(
                        "Learn ethical hacking and penetration testing."
                );

                addSuggestion(
                        "Practice network security and vulnerability analysis."
                );

                addSuggestion(
                        "Earn cybersecurity certifications."
                );

                break;

            // =================================================
            // ANDROID
            // =================================================

            case "Android Developer":

                addSuggestion(
                        "Build Android apps using Java or Kotlin."
                );

                addSuggestion(
                        "Learn Firebase integration and REST APIs."
                );

                addSuggestion(
                        "Publish projects on GitHub portfolio."
                );

                break;
        }
    }

    // =====================================================
    // HIGHLIGHT TEXT
    // =====================================================

    private void highlightText(
            String text,
            String[] keywords
    ) {

        resumeFlow.getChildren().clear();

        String lowerText =
                text.toLowerCase();

        int i = 0;

        while (i < text.length()) {

            boolean matched = false;

            for (String keyword : keywords) {

                if (
                        i + keyword.length()
                                <= text.length()

                                &&

                                lowerText.substring(
                                        i,
                                        i + keyword.length()
                                ).equals(keyword)
                ) {

                    Text highlighted =
                            new Text(
                                    text.substring(
                                            i,
                                            i + keyword.length()
                                    )
                            );

                    highlighted.setStyle(
                            "-fx-fill: #16a34a;" +
                                    "-fx-font-weight: bold;"
                    );

                    resumeFlow.getChildren().add(
                            highlighted
                    );

                    i += keyword.length();

                    matched = true;

                    break;
                }
            }

            if (!matched) {

                Text normal =
                        new Text(
                                String.valueOf(
                                        text.charAt(i)
                                )
                        );

                normal.setStyle(
                        "-fx-fill: #1f2937;"
                );

                resumeFlow.getChildren().add(
                        normal
                );

                i++;
            }
        }
    }

    // =====================================================
    // ADD SUGGESTIONS
    // =====================================================

    private void addSuggestion(String text) {

        Label label =
                new Label("• " + text);

        label.setWrapText(true);

        label.setMaxWidth(Double.MAX_VALUE);

        label.setPrefWidth(300);

        label.getStyleClass().add(
                "suggestion-item"
        );

        suggestionBox.getChildren().add(
                label
        );
    }

    public static void main(String[] args) {

        launch();
    }
}

var io = require("socket.io")(5000);

var namespace = io.of("/");

io.on("connection", function (socket) {

    var id = 1;
    var quizName;
    var quizImage;
    var question;
    var questionImage = [];
    var answers = [];
    var answerImages = {};

    console.log("usuario conectado...");

    socket.on("setQuizId", function (quizId) {
        this.id = quizId;

        socket.join(id);
    });

    socket.on("setAnswer", function (answer, image) {
        answers.push(answer);
        answerImages[answer] = image;
    });

    socket.on("setQuiz", function (quiz, quizThumb) {
        this.quizName = quiz;
        this.quizImage = quizThumb;
    });

    socket.on("setQuestion", function (questionn, questionThumb) {
        this.question = questionn;
        this.questionImage = questionThumb;
    });

    socket.on("", function(){

    });
});
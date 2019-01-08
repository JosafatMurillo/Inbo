
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
    var usernames = {};
    var rooms = {};

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

    //Se agrega una sala nueva a la lista de salas globales
    socket.on("crearSala", function(quizId){
        rooms[quizId] = quizId;
    });

    //Se supone que con esto se transmite la información a todos los usuarios de la sala
    socket.on("iniciarQuiz", function (quiz, quizId){
        socket.broadcast.to(quizId).emit('Preguntas', quiz);
    });

    //El cliente sale de la sala con el mismo id por el cual entró
    socket.on("salir", function(quizId){
        socket.leave(quizId);
    });

    //Se añade un nuevo usuario a la lista de usuarios global con un nombre de usuario y con el id del quiz
    socket.on("addUser", function(username, quizId){
        socket.username = username;
        usernames[username] = username;
        socket.join(quizId);
        socket.broadcast.to(quizId).emit('actualizarSala', 'SERVER', username + ' se ha unido a la sala');
        socket.emit('actualizarSalas', rooms, 'quizId');
    });

    //Cambiar de sala
    socket.on("cambiar", function(salaActual, nuevaSala){
        socket.leave(salaActual);
        socket.join(nuevaSala);
        socket.emit('actualizado', 'SERVER', 'Te has conectado a la sala '+nuevaSala);
        socket.broadcast.to(salaActual).emit('actualizar', 'SERVER', socket.username+' ha abandonado la sala');
        socket.broadcast.to(nuevaSala).emit('actualizar', 'SERVER', socket.username+ 'se ha unido a la sala');
    });
});
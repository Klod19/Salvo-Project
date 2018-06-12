var allGames=[];
$.ajax("/api/games").done(function(data) {
    // alert("data retrieved!");
    allGames.push(data)
    allGames[0].forEach(function(game){
        var listElement = $("<li>")
        var date = new Date(game.created);//converts milliseconds to PRECISE date, with GMT
        // add users emails: make an array with the usernames AND make it into one string with .join()
        var gamePlayersArray = game.gamePlayers;
        var userNames= "";
        var playersArray=[];
        gamePlayersArray.forEach(function(gp){
            console.log(gp);
            playersArray.push(gp.player.userName);
            userNames = playersArray.join(", ");
        })
        // date-toLocaleString() converts the date to a local date, with time too
        listElement.html(date.toLocaleString() + " " + userNames);
        $("#list").append(listElement);
    })
    // WHY DOESN'T ARROW FUNCTION WORK???? TRY MAP
    // var list = allGames[0].map(g)
}).fail(function(){
    alert(data.statusText);
});

//event handlers for login-logout
const login_btn = $("#login_btn");
const logout_btn = $("#logout_btn");
const logout_form = $("#logout-form");
const login_form = $("#login-form");

login_btn.click(function(){
    login(event)
});

logout_btn.click(function(){
    logout(event);
})

//functions to manage login
function login(evt) {
    // why do i need to call event.preventDefault()???
    evt.preventDefault();
    var form = evt.target.form;
    $.post("/api/login",
        { userName: form["userName"].value,
          password: form["password"].value })
        .done(function(){
                alert("welcome!"),
                logout_form.show(),
                login_btn.hide(),
                form["userName"].value = "",
                form["password"].value = "",
                login_form.hide()
        })

        .fail(function(){
            form["password"].value = "",
                alert("Error: Login FAIL")

        })
}

function logout(evt) {
    evt.preventDefault();
    $.post("/api/logout")
        .done(function(){
                alert("Successfully logged out"),
                logout_form.hide(),
                login_form.show(),
                login_btn.show()
        })

        .fail(function(){
            alert("Logout FAIL");
        })

}

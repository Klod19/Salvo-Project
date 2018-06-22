//event handlers for login-logout
const logIn_btn = $("#login_btn");
const signIn_btn = $("#signIn_btn")
const logout_btn = $("#logout_btn");
const logout_form = $("#logout-form");
const login_form = $("#login-form");
const signIn_p = $("#sign_in");
const login_title = $("#login_title")
const welc_msg = $("#welc_msg");
const modal = $("#myModal");
const player_name = $("#player_name");
const newGame_btn = $("#newG_btn");
let name = "";
let allGames=[];

//on the page load, check if there is a cookie
//AND MAKE THE GODDAMN LIST
$(document).ready(function() {
    checkCookie()
    console.log(document.cookie.split("=")[0]);
    let name = getCookie("userName");
    player_name.html(name.split("@")[0]);
    callGames(name);
});


$("#sign_in_popup").click(function(){
    $(".modal-dialog").show();
});

logIn_btn.click(function(){
    login(event)
});

signIn_btn.click(function() {
    signIn(event);
});

logout_btn.click(function(){
    logout(event);
});

newGame_btn.click(function(){
    createGame(event);
});

//functions to manage login
function login(evt) {
    // why do i need to call event.preventDefault()???
    evt.preventDefault();
    var form = evt.target.form;
    $.post("/api/login",
        { userName: form["userName"].value,
          password: form["password"].value })
        .done(function(){
            let current_user = form["userName"].value
            callGames(current_user);
            alert("welcome!"),
            //get the name, get rid of the @, take the first part of the name
            name = (form["userName"].value).split("@")[0];
            console.log(name);
            setCookie("userName",form["userName"].value, 365);
            form["userName"].value = "",
            form["password"].value = "",
            welc_msg.hide(),
            signIn_p.hide()
            player_name.text(name);
            login_form.hide(),
            logout_form.show()

        })
        .fail(function(response){
                alert("ERROR: " + response.responseText);
                form["userName"].value = "",
                form["password"].value = ""
        })
}

function signIn(evt){
    evt.preventDefault();
    let form = evt.target.form;
    let userName = form["userName"].value
    let password = form["password"].value
    if ((userName != "" && !userName.includes(" ")) && (password != "" && !password.includes(" "))){
        $.post("/api/players",
            {   userName: form["userName"].value,
                password: form["password"].value })
            .done(function(){
                    alert("user created");
                    login(evt);
            })
            .fail(function(response){
                    alert("ERROR: " + response.responseText);
                    form["userName"].value = "",
                    form["password"].value = ""
            })
    }
    else{
            alert("ERROR: field empty or space between characters"),
            form["userName"].value = "";
            form["password"].value = "";
    }
}


function logout(evt) {
    evt.preventDefault();
    $.post("/api/logout")
        .done(function(){
                //the following line deletes the cookie
                    document.cookie = "userName=; expires=Thu, 01 Jan 1970 00:00:00 UTC; path=/;"
                    alert("Successfully logged out"),
                    //reload the page;
                    location.reload();
        })

        .fail(function(){
            alert("Logout FAIL");
        })

}

//the following stores the name of the user in a cookie variable
function setCookie(cname, cvalue, exdays) {
    var d = new Date();
    d.setTime(d.getTime() + (exdays * 24 * 60 * 60 * 1000));
    var expires = "expires="+d.toUTCString();
    document.cookie = cname + "=" + cvalue + ";" + expires + ";path=/";
    console.log(document.cookie);
}

//the following returns the value of a specified cookie
function getCookie(cname) {
    var name = cname + "=";
    var ca = document.cookie.split(';');
    for(var i = 0; i < ca.length; i++) {
        var c = ca[i];
        while (c.charAt(0) == ' ') {
            c = c.substring(1);
        }
        if (c.indexOf(name) == 0) {
            return c.substring(name.length, c.length);
        }
    }
    return "";
}

//the following checks if the cookie is set
function checkCookie() {
    var user = getCookie("userName");
    if (user != "") {
                alert("Welcome again " + user);
                welc_msg.hide(),
                login_form.hide(),
                logout_form.show(),
                player_name.html(user),
                signIn_p.hide();


        // } else {
    //     user = prompt("Please enter your name:", "");
    //     if (user != "" && user != null) {
    //         setCookie("username", user, 365);
    //     }
    }
}

function callGames(current_user){
    $.ajax("/api/games").done(function(data) {
        allGames = data.games
        console.log(allGames);
        console.log(current_user);
        makeList(allGames, current_user);
    })
}

//function to create a new game
function createGame (evt) {
    evt.preventDefault();
    $.post("/api/games")
        .done(function(response){
            alert("done" + response.toString());
            let current_user = "";
            console.log(document.cookie);
            location.reload();
        })

        .fail(function(response){
            alert("ERROR" + response.responseText)
        })


}

//TRY WITH MAP AND FILTER, DOESN'T WORK
// function makeList(array, current_userName){
//     array.forEach(function(game){
//         let listElement = $("<li>")
//         let date = new Date(game.created);//converts milliseconds to PRECISE date, with GMT
//         // add users emails: make an array with the usernames AND make it into one string with .join()
//         let gamePlayersArray = game.gamePlayers;
//         console.log(gamePlayersArray);
//         let userNames= "";
//         let gpId = "";
//         let playersArray=[];
//         gamePlayersArray.map(gp => playersArray.push(gp.player.userName));
//         gamePlayersArray.filter(gp => gp.player.userName = current_userName ? gpId = gp.player.gpId : 0);
//         console.log(gpId);
//         userNames = playersArray.join(", ");
//         let url = "http://localhost:8080/web/game.html?gp=" + gpId
//         // date-toLocaleString() converts the date to a local date, with time too
//         let content = $("<p>");
//         content.html(date.toLocaleString() + " " + userNames);
//         if(playersArray.includes(current_userName /*i.e. the form[userName].value*/)){
//             console.log("WORKS")
//             console.log(url);
//             let anchor = $("<a href =' " + url + " ' > ");
//             anchor.append(content)
//             listElement.append(anchor);
//         }
//         else{
//             listElement.append(content);
//         }
//         $("#list").append(listElement);
//
//     })
// }





//THIS WORKS, but is long
function makeList(array, current_userName){
    $("#list").empty();
    array.forEach(function(game){
        let listElement = $("<li>");
        let date = new Date(game.created);//converts milliseconds to PRECISE date, with GMT
        // add users emails: make an array with the usernames AND make it into one string with .join()
        let gamePlayersArray = game.gamePlayers;
        let userNames= "";
        let gpId = "";
        let playersArray=[];
        gamePlayersArray.forEach(function(gp){
            playersArray.push(gp.player.userName);
            console.log(current_userName);
            if(/*playersArray.includes(current_userName) &&*/ current_userName == gp.player.userName ){
                console.log(current_userName);
                gpId = gp.player.gpId;
            }
            userNames = playersArray.join(", ");
        })
        let url = "http://localhost:8080/web/game.html?gp=" + gpId
        // date-toLocaleString() converts the date to a local date, with time too
        let content = $("<p>");
        content.html(date.toLocaleString() + " " + userNames);
        if(playersArray.includes(current_userName /*i.e. the form[userName].value*/)){
            let anchor = $("<a href =' " + url + " ' > ");
            anchor.append(content);
            listElement.append(anchor);
            $("#list").prepend(listElement);
        }
        //if there's no active cookie, show a list of all games
        if (document.cookie == "") {
            listElement.append(content);
            $("#list").prepend(listElement);
        }
    })
}
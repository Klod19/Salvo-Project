var allGames=[];
$.ajax("/api/games").done(function(data) {
    alert("data retrieved!");
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


// var date = '1475235770601';
// var d = new Date(parseInt(date, 10));
// var ds = d.toString('MM/dd/yy HH:mm:ss');
// console.log(ds);
//this works
// fetch( "/api/games").then(function(response) {
//     console.log('Request succeeded: ' + response.statusText);
// }).catch(function(error) {
//     console.log( "Request failed: " + error.message );
// });
//this works too
    // $.ajax({url: "/api/games", success: function(result){
//     //         alert("success!");
//     //     }});
// let us use the following:

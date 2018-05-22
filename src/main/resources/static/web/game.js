// array to fill the header;
var num_array = [ 1, 2, 3, 4, 5, 6, 7, 8, 9, 10];
var ltrs_array = ["A", "B", "C", "D", "E", "F", "G", "H", "I", "J"];

var salvo_array_one = [
    { "gamePlayer_id": "1", "turn": "1", "player": "1", "locations": ["H1", "A2"] },
    { "gamePlayer_id": "2", "turn": "1", "player": "2", "locations": ["A8", "F10"] },
    { "gamePlayer_id": "1", "turn": "2", "player": "1", "locations": ["B4", "D8"] },
    { "gamePlayer_id": "2", "turn": "2", "player": "2", "locations": ["H7", "D10"] }
];

//FIRST OF ALL:
// GET THE PAGE "gp" PARAMETER, IN ORDER TO USE IT FOR AJAX CALL; 3 WAYS

// 1)SOLUTION FROM STACKOVERFLOW, FROM THE TASK SUGGESTION
// https://stackoverflow.com/questions/901115/how-can-i-get-query-string-values-in-javascript
// var urlParams;
// (window.onpopstate = function () {
//     var match,
//         pl     = /\+/g,  // Regex for replacing addition symbol with a space
//         search = /([^&=]+)=?([^&]*)/g,
//         decode = function (s) { return decodeURIComponent(s.replace(pl, " ")); },
//         query  = window.location.search.substring(1);
//
//     urlParams = {};
//     while (match = search.exec(query))
//         urlParams[decode(match[1])] = decode(match[2]);
// })();
//

//2) SOLUTION FROM JS EBOOK, CHAPTER 18; I DON'T UNDERSTAND AND IT MISSES SOMETHING; MUST
//ADD A WAY TO CHOOSE NUMBER OF PARAMETER "search"
// function paramObj(search) {
//     var obj = {};
//     var reg = /(?:[?&]([^?&#=]+)(?:=([^&#]*))?)(?:#.*)?/g;
//
//     search.replace(reg, function(match, param, val) {
//         obj[decodeURIComponent(param)] = val === undefined ? "" : decodeURIComponent(val);
//     });
//     console.log(obj);
//     return obj;
// }
// //so i get the gp parameter as an object;
// var page_param =  paramObj("?gp=1");
// console.log(page_param);
// console.log(page_param.gp);

//3)SOLUTION FROM STACKOVERFLOW (see below); THIS WORKS, I DONT UNDERSTAND IT
https://stackoverflow.com/questions/19491336/get-url-parameter-jquery-or-how-to-get-query-string-values-in-js
    function urlParam(name) {
        var results = new RegExp('[\?&]' + name + '=([^&#]*)').exec(window.location.href);
        if (results == null) {
            return null;
        }
        else {
            return decodeURI(results[1]) || 0;
        }
    }

var par = urlParam('gp'); // 1
console.log(par);

// SECOND STEP: I use the obtained parameter to make an AJAX call; from:
//https://stackoverflow.com/questions/31321402/how-to-pass-javascript-variables-inside-a-url-ajax
//first I make an URL
// function makeUrl() {
//     var pageId =  page_param.gp;
//     var url = "/api/gamePlayer/" + pageId;
//     return url;
// }
//
// function doAjax(_url) {
//     return $.ajax({
//         url: _url,
//         type: 'GET'
//     });
// }
//
// doAjax(makeUrl()).done(function (data) {
//     alert("data retrieved");
// })
// actually, my method is easier:
//get the pageId from the parameter obtained earlier
// var pageId =  page_param.gp;

var pageId = par;
console.log("PAGE ID:" + pageId);
var allShips =[];
var loc_array=[];
var gamePlayers =[];
var salvoesArray =[]
$.ajax("/api/game_view/" + pageId).done(function(data){
    allShips.push(data.ships);
    salvoesArray.push(data.salvoes);
    //salvoesArrayy is an array of 2 arrays of objects; let's concatenate the 2 arrays of objects:
    var allSalvoes = salvoesArray[0][0].concat(salvoesArray[0][1]);
    gamePlayers.push(data.gamePlayers);
    console.log(gamePlayers);
    console.log(data);
    console.log(allShips);
    //call the functions to make the empty grid
    renderHeaders();
// renderRows();
    makeTable();
    //call the functions to place the ships
    getLocations(allShips);
    //call the function to render the two players' names
    getPlayers(gamePlayers[0]);
}).fail(function(){
    alert("ERROR DATA NOT RETRIEVED")
});

//following: the 3 functions to place the ships
function getLocations(ships_array){
    for (var i=0; i<5; i++){
        var loc = ships_array.map(s => s[i].location);
        console.log(loc);
        loc_array.push(loc);
    }
    placeShips(loc_array);
    console.log(loc_array);
}

function placeShips(array){
    array.map(l => l.map(coord => get_coordinates(coord)));
}

function get_coordinates(coord){
    console.log(coord);
    //SIMPLE SOLUTION WITH EASIER COORDINATES
    coord.forEach(function(c){
        var one_cell = $("#"+c);
        $(one_cell).css("background-color", "yellow");
        salvo_array_one.forEach(function(s){
            if (s.gamePlayer_id != pageId && (s.locations[0] == $(one_cell).attr("id")
                || s.locations[1]  == $(one_cell).attr("id"))){
                $(one_cell).css("background-color", "red");
                $(one_cell).html(s.turn);

                s.locations.forEach(function(lo){
                    if (s.gamePlayer_id != pageId && lo != $(one_cell).attr("id")){
                        var missed = $("#"+lo)
                        $(missed).css("background-color", "aquamarine");
                        $(missed).html(s.turn)
                    }
                })
            }

        });
    });
    salvo_array_one.forEach(function(ms){
       if(ms.gamePlayer_id == pageId){
           ms.locations.forEach(function(msl){
           var my_salvo_cell = $("#salvo_cell"+msl);
           my_salvo_cell.css("background-color", "blue");
           my_salvo_cell.html(ms.turn);
           })
       }
    })

}

//FOLLOWING: ADD CURRENT PLAYER + OPPONENT
function getPlayers(array){
    array.forEach(function(gp){
        if(gp.id == pageId){
            $("#current_user").html(gp.player.userName);
        }
        else {
            $("#opponent").html(gp.player.userName);
        }
    })
}

// WHY .index() didn't work?? how can i get ALL the elements from document
//BECAUSE IT SHOULD BE: one_cell.index(one_cell[number])

//FOLLOWING: TWO DIFFERENT WAYS TO MAKE THE EMPTY GRID
// this function makes an header row , with the first column empty, and with the other columns filled
// with the numbers from the array
function makeNumberHeaders() {
    return "<tr> <th></th>" + num_array.map(function(num) {
        return "<th>" + num + "</th>";
    }).join("") + "</tr>";

}
//this function renders the header as the html content of "table headers"
function renderHeaders() {
    var html = makeNumberHeaders();
    $("#table-headers").html(html);
    $("#salvoes-header").html(html);
}

//this loops through the array of letters, making empty columns for each letter; it is
//called inside the following function ,as a nested loop
function getColumnsHtml() {
    return ltrs_array.map(function (cell, i) {
        var t_data = $("<td>");
        t_data.attr("id", "cell_" + i);
        return "<td></td>";
    }).join("")

}
// this loops through the number array, making the 1st column with a letter and the other columns
//empty through the function getColumnsHtml
function getRowsHtml() {
    return num_array.map(function(n, i) {
        return "<tr><td id='"+ i +"'>" + ltrs_array[i] + "</td>" +
            getColumnsHtml() + "</tr>";

    }).join("");
}

//this simply renders the rows
function renderRows () {
    var html = getRowsHtml();
    $("#table-rows").html(html);
}

// OR : make the table with a for loop (my solution, easier to assign ids;
function makeTable(){//makes the table for the ships AND the table for the salvoes
    for (var i = 0; i < ltrs_array.length; i++){
        var row = $("<tr>").attr("id", "row_" + i)
        var salvo_row = $("<tr>").attr("id", "salvo_row_" + i)
        $("#table-rows").append(row);
        $("#salvoes-rows").append(salvo_row);

        console.log(row.index());;
        for (var k = 0; k <= num_array.length; k++){
            // var cell = $("<td>").attr("id", "cell_id_" + k + "_row_" + i )
            var cell = $("<td>");
            var salvo_cell= $("<td>");
            $(row).append(cell);
            $(salvo_row).append(salvo_cell);
            if(cell.index() === 0){
                cell.attr({"id": "side_cell_"+i, "class": "side_letters"});
                cell.html(ltrs_array[i]);
                salvo_cell.attr({"id": "salvo_side_cell"+i, "class" : "side_letters"});
                salvo_cell.html(ltrs_array[i]);
            }
            else{
                cell.attr({"id" : ltrs_array[i]+k , "class": "table_cells"});
                cell.html("");
                salvo_cell.attr({"id": "salvo_cell"+ ltrs_array[i]+k, "class": "table_cells"});
                salvo_cell.html("");
            };
        }
    }
}


// THE FOLLOWING DOESN'T WORK; WHY?
$("#cell_id_1_row_0").click(function(){
    alert("LOL")
    // alert("My position in table is X: " + this.cellIndex + "x" + this.tr.rowIndex);
});

//THE FOLLOWING WORKS ONLY IF I CHANGE ".table_cells" with "#table-rows"
$(".table_cells").on("click", function(){
    alert("LOL");
    console.log(this.id);
})
// the following is not that useful
// $("#table-rows").on("click","td", function(e){
//     var parent = e.target.parentElement;
//     var id = $(parent).attr("id");
//
// })








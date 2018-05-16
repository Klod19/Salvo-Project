// array to fill the header;
var num_array = [ 1, 2, 3, 4, 5, 6, 7, 8, 9, 10];
var ltrs_array = ["A", "B", "C", "D", "E", "F", "G", "H", "I", "J"];
var characters = {
    'A': 0,
    'B': 1,
    'C': 2,
    'D': 3,
    'E': 4,
    'F': 5,
    'G': 6,
    'H': 7,
    'I': 8,
    'J': 9,
}

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

//2)SOLUTION FROM STACKOVERFLOW (see below); THIS WORKS, I DONT UNDERSTAND IT
//https://stackoverflow.com/questions/19491336/get-url-parameter-jquery-or-how-to-get-query-string-values-in-js
// $.urlParam = function(name) {
//     var results = new RegExp('[\?&]' + name + '=([^&#]*)').exec(window.location.href);
//     if (results == null) {
//         return null;
//     }
//     else {
//         return decodeURI(results[1]) || 0;
//     }
// }
//
// var par = $.urlParam('gp'); // 1
// console.log(par);

//3) SOLUTION FROM JS EBOOK, CHAPTER 18; I DON'T UNDERSTAND IT BUT IT WORKS
function paramObj(search) {
    var obj = {};
    var reg = /(?:[?&]([^?&#=]+)(?:=([^&#]*))?)(?:#.*)?/g;

    search.replace(reg, function(match, param, val) {
        obj[decodeURIComponent(param)] = val === undefined ? "" : decodeURIComponent(val);
    });

    return obj;
}
//so i get the gp parameter as an object;
var page_param =  paramObj("?gp=1");
console.log(page_param.gp);

// now I use the obtained parameter to make an AJAX call; from:
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
var pageId =  page_param.gp;
var allShips =[];
$.ajax("/api/game_view/" + pageId).done(function(data){
    allShips.push(data.ships);
    console.log(data);
    console.log(allShips);
    //the following gets five arrays made out of arrays; the latter are 2-elements arrays,
    //containing letter-number
    var loc_array=[];
    for (var i=0; i<5; i++){
        var loc = allShips.map(s => s[i].location.map(l => l.toString().split ("", 2)));
        console.log(loc);
        loc_array.push(loc);
    }
    console.log(loc_array);
    function placeShips(){
        loc_array.map(l => l.map(coord => get_coordinates(coord)));
    }
    placeShips();
}).fail(function(){
    alert("ERROR DATA NOT RETRIEVED")
});

function get_coordinates(coord){
    console.log(coord);
    for (var i = 0; i < coord.length; i++){
        var number = characters[coord[i][0]];
        var y = number;
        var x = coord[i][1];
        // var one_cell = document.getElementsByTagName("td");
        var one_cell = $("#cell_id_" + x + "_row_" + y );
        $(one_cell).css("background-color", "yellow");
        // if( $(one_cell).parent().attr("id") === "row_" + y &&
        //                             $(one_cell).attr("id") === "cell_id_"+ x + "_row_" + y ){
        //     $(one_cell).css("background-color", "yellow");
        // }
        // WHY .index() didn't work?? how can i get ALL the elements from document
    }

}


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
        return "<tr><td>" + ltrs_array[i] + "</td>" +
            getColumnsHtml() + "</tr>";

    }).join("");
}

// make the table with a for loop
function makeTable(){
    for (var i = 0; i < ltrs_array.length; i++){
        var row = $("<tr>").attr("id", "row_" + i)
        $("#table-rows").append(row);
        console.log(row.index());;
        for (var k = 0; k <= num_array.length; k++){
            var cell = $("<td>").attr("id", "cell_id_" + k + "_row_" + i )
            $(row).append(cell);
            if(cell.index() === 0){
                cell.attr("class", "side_letters")
                cell.html(ltrs_array[i])
            }
            else{
                cell.attr("class", "table_cells")
                cell.html("");
            };
        }
    }
}

//this simply renders the rows
function renderRows () {
    var html = getRowsHtml();
    $("#table-rows").html(html);
}


// THE FOLLOWING DOESN'T WORK; WHY?
$("#cell_id_1_row_0").click(function(){
    alert("LOL")
    // alert("My position in table is X: " + this.cellIndex + "x" + this.tr.rowIndex);
});

//THE FOLLOWING WORKS ONLY IF I CHANGE ".table_cells" with "#table-rows"
// $(".table_cells").on("click","td", function(){
//     alert("LOL");
//     console.log("works");
// })
// the following is not that useful
$("#table-rows").on("click","td", function(e){
    var parent = e.target.parentElement;
    var id = $(parent).attr("id");

})


renderHeaders();
// renderRows();
makeTable();




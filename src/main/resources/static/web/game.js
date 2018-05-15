// array to fill the header;
var num_array = [ 1, 2, 3, 4, 5, 6, 7, 8, 9, 10];
var ltrs_array = ["A", "B", "C", "D", "E", "F", "G", "H", "I", "J"];

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

//3) SOLUTION FROM JS EBOOK, CHAPTER 18; I DON't UNDERSTAND IT BUT IT WORKS
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

// actually my method is easier
var pageId =  page_param.gp;
$.ajax("/api/game_view/" + pageId).done(function(data){
    alert("data retrieved")

}).fail(function(){
    alert("ERROR DATA NOT RETRIEVED")
});




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
    return ltrs_array.map(function() {
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

//this simply renders the rows
function renderRows () {
    var html = getRowsHtml();
    $("#table-rows").html(html);
}

renderHeaders();
renderRows();


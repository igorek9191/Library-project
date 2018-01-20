$(document).ready(function () {

    $("#addBook").click(function () {
        console.log('BOOK');
        var book = {
            bookID: $("#bookID").val(),
            bookName: $("#bookName").val()
        };
        console.log('BOOK', book);

        $.ajax({
            url:"/bookController/book/add",
            type:"POST",
            dataType: 'json',
            data: JSON.stringify(book),
            contentType:"application/json; charset=utf-8",
            success: function (result) {
                console.log(result);
                alert(JSON.stringify(result));
            },
            error: function(error) {
                console.log(error);
                alert(JSON.stringify(error));
            }
        });
        //clearFields();
    });

    $("#editBook").click(function () {
        console.log('BOOK');
        var book = {
            bookID: $("#bookID").val(),
            bookName: $("#bookName").val()
        };
        console.log('BOOK', book);

        $.ajax({
            url:"/bookController/book/edit",
            type:"PUT",
            data: JSON.stringify(book),
            contentType:"application/json; charset=utf-8",
            success: function (result) {
                console.log(result);
                alert(JSON.stringify(result));
            },
            error: function(error) {
                console.log(error);
                alert(JSON.stringify(error));
            }
        });
        //clearFields();
    });

    $("#deleteBook").click(function () {
        console.log('BOOK');
        var book = {
            bookID: $("#bookID").val(),
            bookName: $("#bookName").val()
        };
        console.log('BOOK', book);

        $.ajax({
            url:"/bookController/book/delete",
            type:"DELETE",
            data: JSON.stringify(book),
            contentType:"application/json; charset=utf-8",
            success: function (result) {
                console.log(result);
                alert(JSON.stringify(result));
            },
            error: function(error) {
                console.log(error);
                alert(JSON.stringify(error));
            }
        });
        //clearFields();
    });

    $("#bookCatalog").click(function () {
        $.ajax({
            url:"/bookController/book/all",
            type:"GET",
            contentType:"application/json; charset=utf-8",
            dataType:"json",
            success: function(result){
                console.log(result);
                alert(JSON.stringify(result));
            },
            error: function(error) {
                console.log(error);
                alert(JSON.stringify(error));
            }
        });
    });

});

var clearFields = function () {
    $("#bookID").val('');
    $("#bookName").val('');
};
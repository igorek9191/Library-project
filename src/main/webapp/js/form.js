$(document).ready(function () {

    $("#giveBookToPerson").click(function () {
        console.log('GIVE_BOOK_TO_PERSON');
        var giveBookToPerson = {
            bookID: $("#bookID").val(),
            bookName: $("#bookName").val(),

            fullName: $("#fullName").val(),
            phoneNumber: $("#phoneNumber").val()
        };
        console.log('giveBookToPerson', giveBookToPerson);

        $.ajax({
            url:"/giveBookToPerson",
            type:"POST",
            data: JSON.stringify(giveBookToPerson),
            contentType:"application/json; charset=utf-8",
            success: function (result) {
                console.log(result);
                alert(JSON.stringify(result));
            },
            error: function(responseJSON) {
                console.log(responseJSON);
                alert(JSON.stringify(responseJSON));
            }
        });
        //clearFields();
    });

    $("#takeBackBookFromPerson").click(function () {
        console.log('TAKE_BAKE_BOOK_FROM_PERSON');
        var takeBackBookFromPerson = {

            bookID: $("#bookID").val(),
            bookName: $("#bookName").val(),

            fullName: $("#fullName").val(),
            phoneNumber: $("#phoneNumber").val()

        };
        console.log('takeBackBookFromPerson', takeBackBookFromPerson);

        $.ajax({
            url:"/takeBackBookFromPerson",
            type:"POST",
            data: JSON.stringify(takeBackBookFromPerson),
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

    $("#checkBooksOfPerson").click(function () {
        console.log('CHECK_BOOKS_OF_PERSON');
        var checkBooksOfPerson = {

            fullName: $("#fullName").val(),
            phoneNumber: $("#phoneNumber").val()

        };
        console.log('checkBooksOfPerson', checkBooksOfPerson);

        $.ajax({
            url:"/checkBooksOfPerson",
            type:"POST",
            data: JSON.stringify(checkBooksOfPerson),
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

    $("#checkBusyBooks").click(function () {
        console.log('CHECK_BUSY_BOOKS');

        $.ajax({
            url:"/checkBusyBooks",
            type:"GET",
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

    });

    $("#checkFreeBooks").click(function () {
        console.log('CHECK_FREE_BOOKS');

        $.ajax({
            url:"/checkFreeBooks",
            type:"GET",
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

    });

    $("#checkPersonHistory").click(function () {
        console.log('CHECK_PERSON_HISTORY');
        var checkPersonHistory = {
            fullName: $("#fullName").val(),
            phoneNumber: $("#phoneNumber").val()
        };
        console.log('checkPersonHistory', checkPersonHistory);

        $.ajax({
            url:"/checkPersonHistory",
            type:"POST",
            data: JSON.stringify(checkPersonHistory),
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

    $("#checkBookHistory").click(function () {
        console.log('CHECK_BOOK_HISTORY');
        var checkBookHistory = {
            bookID: $("#bookID").val(),
            bookName: $("#bookName").val()
        };
        console.log('checkBookHistory', checkBookHistory);

        $.ajax({
            url:"/checkBookHistory",
            type:"POST",
            data: JSON.stringify(checkBookHistory),
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

});

var clearFields = function () {
    $("#bookID").val(''),
    $("#bookName").val(''),
    $("#fullName").val(''),
    $("#phoneNumber").val('')
};
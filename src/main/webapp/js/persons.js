$(document).ready(function () {

    $("#addPerson").click(function () {
        console.log('PERSON');
        var person = {
            fullName: $("#fullName").val(),
            phoneNumber: $("#phoneNumber").val()
        };
        console.log('PESON', person);

        $.ajax({
            url:"/personController/person/add",
            type:"POST",
            data: JSON.stringify(person),
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

    $("#editPerson").click(function () {
        console.log('PERSON');
        var personEdit = {
            oldFullName: $("#fullName").val(),
            oldPhoneNumber: $("#phoneNumber").val(),
            newFullName: $("#newFullName").val(),
            newPhoneNumber: $("#newPhoneNumber").val()
        };
        console.log('PESON', personEdit);

        $.ajax({
            url:"/personController/person/edit",
            type:"POST",
            data: JSON.stringify(personEdit),
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

    $("#deletePerson").click(function () {
        console.log('PERSON');
        var person = {
            fullName: $("#fullName").val(),
            phoneNumber: $("#phoneNumber").val()
        };
        console.log('PERSON', person);

        $.ajax({
            url:"/personController/person/delete",
            type:"DELETE",
            data: JSON.stringify(person),
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

    $("#allPersons").click(function () {
        $.ajax({
            url:"/personController/persons/all",
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
    $("#fullName").val('');
    $("#phoneNumber").val('');
    $("#newFullName").val('');
    $("#newPhoneNumber").val('')
};
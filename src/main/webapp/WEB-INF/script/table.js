// Function to trigger url/action to download all users data as an excel file
$('#download').click(function(){
    console.log("sending request to download user data...");
    window.location.href = `download-file`;
});

// Search functionality for VIEW USERS page
$(document).ready(function () {
    $("#searchText").on("keyup", function () {
        var value = $(this).val().toLowerCase();
        var matchCount = 0;
        // hide pagination when searching
        if(value) {
            $(".pagination").hide();
        } else {
            $(".pagination").show();
        }

        $("#userTable tr").not("#no-data-row").each(function () {
            var isMatch = $(this).text().toLowerCase().indexOf(value) > -1;
            $(this).toggle(isMatch);
            if (isMatch) matchCount++;
        });

        // if no rows matched, show no data row
        if (matchCount === 0) {
            $("#no-data-row").show();
            } else {
            $("#no-data-row").hide();
        }
    });
});

// function to be called to remove user data
function removeUser(userId, fullName) {
    console.log("inside removeUser >> userId::", userId, " && fullName:: ", fullName);
    let confirmValue = confirm("Are you sure ? All the details of '" + fullName + "' will be permanently deleted");
    console.log("confirmValue : " + confirmValue);
    if (confirmValue) {
        deleteRecord(userId);
    }
}

// function to send ajax request to servlet to delete user data
function deleteRecord(userId) {
    console.log("delete record called for userid: ", userId);
    $.ajax({
        url: `delete-user`,
        type: 'POST',
        dataType: 'json',
        data: { userid: userId },
        success: function (data) {
            console.log("Success >> ", data);
            if (data.success === true) {
                alert("User data has been deleted successfully");
                location.reload();
            } else {
                console.log("delete message >> ", data.message);
                alert(data.message || "An unexpected error occurred");
                location.reload();
            }
        },
        error: function(jqXhr, textStatus, errorMessage) {
            console.log("delete request failed", errorMessage);
            alert(`Delete request failed: ${errorMessage}`);
        }
    });
}
// Function to trigger download file url/action
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

        $("#tableBody tr").not("#no-data-row").each(function () {
            var isMatch = $(this).text().toLowerCase().indexOf(value) > -1;
            $(this).toggle(isMatch);
            if (isMatch) matchCount++;
        });

        // if no rows matched, show no data row
        if (matchCount === 0) {
            toggleNoDataRow(true);
        } else {
            toggleNoDataRow(false);
        }
    });
});

// function to show/hide no-data row
function toggleNoDataRow(showRow) {
    console.log("toggleNoDataRow >> showRow : ", showRow);
    if(showRow === true) {
        $("#no-data-row").show();
        $('#userTable').addClass("width-auto");
        $('#userTable').removeClass("max-content-width");
        return;
    }
    $("#no-data-row").hide();
    $('#userTable').addClass("max-content-width");
    $('#userTable').removeClass("width-auto");
}

// function to populate table rows based users details
var currentPageValue = 1;
function renderUserTable(response) {
    let users = response.users;
    console.log("json users >> ", users);
    var $tableBody = $("#tableBody");
    $tableBody.empty();

    // no data row
    var $row = $('<tr id="no-data-row"></tr>');
    var $cell = $("<td></td>")
        .attr("colspan", 7)
        .html('<span class="no-data">No Data Available</span>');
    $row.append($cell);
    $tableBody.append($row);

    if (!users && users.length === 0) {
        toggleNoDataRow(true);
        return;
    }
    // else hide no data row
    toggleNoDataRow(false);

    // create user detail rows
    users.forEach(function (user) {
        var $row = $("<tr></tr>");
        $("<td></td>").text(user.userId).appendTo($row);
        $("<td></td>").text(user.firstName).appendTo($row);
        $("<td></td>").text(user.lastName).appendTo($row);
        $("<td></td>").text(user.email).appendTo($row);
        $("<td></td>").text(user.dob).appendTo($row);
        $("<td></td>").text(user.gender).appendTo($row);
        // Create the action buttons cell
        var $actions = $('<td></td>').html(
            `<a class="action-btn" href="edit-user?userid=${user.userId}">Update</a> ` +
            `<a class="action-btn rm-btn" onclick="removeUser('${user.userId}', '${user.firstName} ${user.lastName}')">Remove</a>`
        );
        $row.append($actions);
        $tableBody.append($row);
    });
    // show pagination
    renderPagination(response.totalPages, response.currentPage);
}

// function to render pagination buttons
function renderPagination(totalPages, currentPage) {
    console.log("rendering pagination..", totalPages, currentPage);
    currentPageValue = currentPage;
    const $pagination = $('#pagination');
    $pagination.empty();

    if (totalPages === 0) return;

    $pagination.append(`<a href="#" data-page="1">&laquo; First</a>`);

    for (let i = 1; i <= totalPages; i++) {
        const activeClass = i === currentPage ? 'active' : '';
        $pagination.append(`<a href="view-users?page=${i}" class="${activeClass}" data-page="${i}">${i}</a> `);
    }

    $pagination.append(`<a href="#" data-page="${totalPages}">Last &raquo;</a>`);
}

// function to
handle pagination link clicks
$(document).on('click', '#pagination a', function(e) {
    e.preventDefault();
    const selectedPage = parseInt($(this).data('page'));
    if (!isNaN(selectedPage)) {
        currentPage = selectedPage;
        makeAjaxRequest('get-all-users?page='+currentPage, renderUserTable);
    }
});

// DELETE FUNCTIONALITY
// function to be called to remove user data
function removeUser(userId, fullName) {
    console.log("inside removeUser >> userId::", userId, " && fullName:: ", fullName);
    let confirmValue = confirm("Are you sure ? All the details of '" + fullName + "' will be permanently deleted");
    console.log("confirmValue : " + confirmValue);
    if (confirmValue) {
        makeAjaxRequest('delete-user?userid='+userId, handleDeleteResponse);
    }
}

// function to handle response for the delete request
function handleDeleteResponse(response) {
    console.log("deleteRecord called data: ", response);
    if (response.success === true) {
        alert("User details has been deleted successfully");
    } else {
        console.log("delete message >> ", response.message);
        alert(response.message || "An unexpected error occurred");
    }
    //location.reload();
    makeAjaxRequest('get-all-users?page='+currentPageValue, renderUserTable);
}
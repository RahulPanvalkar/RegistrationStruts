    // NAVBAR
    $(document).ready(function() {
    	$(".navlink").click(function() {
    		console.log("activating nav link")
    	  // remove classes from all
    	  $(".navlink").removeClass("active");
    	  // add class to the one we clicked
    	  $(this).addClass("active");

    	  return false;
    	});
    });

    // GENERIC FUNCTION TO SEND AJAX REQUEST
    function makeAjaxRequest(URL, callback) {
        console.log("sending ajax request..");
        $.ajax({
            url: URL,
            type: 'GET',
            dataType: 'json',
            success: function(response) {
                console.log("makeAjaxRequest >> response >> ", response);
                callback(response);
            },
            error: function(jqXhr, textStatus, errorMessage) {
                console.error("Error occurred: ", errorMessage);
                callback([]);
            }
        });
    }

    // FOR REGISTRATION and EDIT USER DETAILS PAGE
    let isEditMode = false;
    let isRepeatRequest = false;
    $(document).ready(function(){

    	// Initialize Datepicker for DOB field and set min max value
        $("#dobPicker").datepicker({
            changeMonth: true,
            changeYear: true,
            yearRange: "-100:-18", // to select from 100 to 18 years ago
            dateFormat: "dd/mm/yy",
            defaultDate: "-18y",   // default position to 18 years ago
            maxDate: "-18Y",       // Max date is 18 years ago
            minDate: "-100Y"       // Min date is 100 years ago
        });

    	// To show and hide password
    	$('#show-password').click(function() {
    	    console.log("show password clicked");

    	    const passField = $('#password');
    	    const type = passField.attr('type');

    	    if (type === 'password') {
    	        passField.attr('type', 'text');
    	        $(this).attr("src", "./images/show.png");
    	    } else {
    	        passField.attr('type', 'password');
    	        $(this).attr("src", "./images/hide.png");
    	    }
    	});


    	// map to tract user input errors
    	let errors = new Map([
    		["userid", true],
    		["fname", true],
    		["lname", true],
    		["email", true],
    		["dob", true],
    		["pass", true],
    		["terms",true],
    		["captcha",true]
    	]);

        // check if hidden field 'editMode' is present in form
        if ($('#editMode').length) {
            isEditMode = $('#editMode').val() === 'true';
        }

        // remove fields from map and trigger validation for other fields
        if(isEditMode === true) {
            console.log("isEditMode is true..");
            errors.delete("pass");	// avoid password field check
            errors.delete("terms"); // avoid term field check
            errors.delete("captcha"); // avoid captcha field check

            // validate other fields at start
            errors.set("userid",!validateUserId());
            errors.set("fname",!validateFirstname());
            errors.set("lname",!validateLastname());
            errors.set("email",!validateEmail());
            errors.set("dob",!validateDob());

            editModeValidation(errors);
        } else {
            errors.delete("userid");
        }


        console.log("isRepeatRequest >> ", isRepeatRequest);
        if(isRepeatRequest) {
            console.log("isRepeatRequest is true..");
            // validate fields
            errors.set("fname",!validateFirstname());
            errors.set("lname",!validateLastname());
            errors.set("email",!validateEmail());
            errors.set("pass",!validatePassword());
            errors.set("dob",!validateDob());

            checkErrors(errors);
        }

    	// Validate first name
    	$("#fnameCheck").hide();
        $("#fname").on("input blur", function () {
    		// if valid store error=false else true
            errors.set("fname",!validateFirstname());
    		checkErrors(errors);
        });

    	//Validate last name
    	$("#lnameCheck").hide();
        $("#lname").on("input blur", function () {
    		// if valid store false else true
            errors.set("lname",!validateLastname());
    		checkErrors(errors);
        });

    	//Validate email
    	$("#emailCheck").hide();
        $("#email").on("input blur", function () {
    		// if valid store false else true
            errors.set("email",!validateEmail());
    		checkErrors(errors);
        });


    	//Validate DOB
    	$("#dobCheck").hide();
        $("#dobPicker").on("change blur", function() {
    		// if valid store false else true
            errors.set("dob",!validateDob());
    		checkErrors(errors);
        });


        //Validate password
    	if ($("#password").length) {
            $("#passwordCheck").hide();
            $("#password").on("input blur", function () {
                // if valid store false else true
                errors.set("pass",!validatePassword());
                checkErrors(errors);
            });
        } else {
            errors.delete("pass");
        }


    	// Disable/enable the register button based on terms & conditions checkbox.
    	if ($("#acceptBox").length) {
    		$('#acceptBox').click(function(){
    			if($(this).is(':checked')) {
    				errors.set("terms",false);
    			} else {
    				errors.set("terms",true);
    			}
    			checkErrors(errors);
    		});
    	} else {
    		errors.delete("terms");
    	}


    	//Validate captcha text
    	if ($("#captchaTxt").length) {
            $("#captchaCheck").hide();
            $("#captchaTxt").on("blur", function () {
                errors.set("captcha",!validateCaptcha());
                checkErrors(errors);
            });
        } else {
            errors.delete("terms");
        }
    });

    // function to check user input errors and if any occurred, disable Register button
    function checkErrors(errors) {
    	console.log(errors);
    	let isAllValid = true;

    	for (let [key, value] of errors) {
    		//console.log(key,"::",value);
    		if(value === true) {	// If any error is true mark as invalid
    			isAllValid = false;
    			break;
    		}
    	}
    	console.log("isAllValid", isAllValid);
    	// if isAllValid is false disable register button
    	toggleRegisterBtn(isAllValid);
    }

    // function to enable or disable register button
    function toggleRegisterBtn(isEnable){
        if (isEditMode) {
            const btn = $('#updtBtn');
            toggleButton(btn, isEnable);
        } else {
            const btn = $('#regBtn');
            toggleButton(btn, isEnable);
        }
    }

    function toggleButton(button, enable){
        if (enable) {
            button.removeAttr('disabled').removeClass('btn-disabled').addClass('btn-enabled');
        } else {
            button.attr('disabled', 'disabled').removeClass('btn-enabled').addClass('btn-disabled');
        }
    }

    // Function for Edit/update page validation
    function editModeValidation(errors){
    	console.log("editModeValidation : errors : ", errors);
    	let isAllValid = true;

    	for (let [key, value] of errors) {
    		console.log(key,"::",value);
    		if(value === true) {	// If any error is true mark as invalid
    			isAllValid = false;
    			break;
    		}
    	}
    	console.log("isAllValid", isAllValid);
    	// if isAllValid is false disable register button
    	toggleRegisterBtn(isAllValid);
    }

    // function to validate first name
    function validateFirstname() {
    	console.log("validateFirstName is called..");
    	let fnameValue = $("#fname").val();
    	console.log(fnameValue);

    	let regex = /^[A-Za-z]+$/;

       	if (!fnameValue) {
    		$("#fnameCheck").show();
    		$("#fnameCheck").html("First name is missing");
            return false;
        } else if (!regex.test(fnameValue)) {
    	        $("#fnameCheck").show();
    	        $("#fnameCheck").html("only letters are allowed");
    			$("#fname").html("");
    	        return false;
    	} else if (fnameValue.length < 3 || fnameValue.length > 15) {
            $("#fnameCheck").show();
            $("#fnameCheck").html("length of first name must be between 3 and 15");
            return false;
        }

        $("#fnameCheck").hide();
        return true;
    }

    // function to validate last name
    function validateLastname() {
    	console.log("validateLastName is called..");
    	let lnameValue = $("#lname").val();
    	console.log(lnameValue);

    	let regex = /^[A-Za-z]+$/;

       	if (!lnameValue) {
    		$("#lnameCheck").show();
    		$("#lnameCheck").html("Last name is missing");
            return false;
        } else if (!regex.test(lnameValue)) {
    	        $("#lnameCheck").show();
    	        $("#lnameCheck").html("only letters are allowed");
    	        return false;
    	} else if (lnameValue.length < 3 || lnameValue.length > 15) {
            $("#lnameCheck").show();
            $("#lnameCheck").html("length of last name must be between 3 and 15");
            return false;
        }

        $("#lnameCheck").hide();
        return true;
    }

    // function to validate email id
    function validateEmail() {
    	console.log("validateEmail is called..");
    	let emailValue = $("#email").val();
    	console.log(emailValue);

    	// regex for valid email format
    	let regex = /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/;

       	if (!emailValue) {
    		$("#emailCheck").show();
    		$("#emailCheck").html("Email id is missing");
            return false;
    	} else if (!regex.test(emailValue)) {
            $("#emailCheck").show();
            $("#emailCheck").html("Invalid email id");
            return false;
    	}

        $("#emailCheck").hide();
        return true;
    }


    // function to validate DOB
    function validateDob() {
    	console.log("validateDob is called..");
    	let dobValue = $("#dobPicker").val();
    	console.log(dobValue, typeof dobValue);

    	const inputDate = new Date(dobValue);
    	const currentDate = new Date();
    	if (!dobValue) {
    		$("#dobCheck").show();
    		$("#dobCheck").html("Date of birth is missing");
            return false;
    	}

    	// calculate the age and check if above 18
    	let age = currentDate.getFullYear() - inputDate.getFullYear();
    	const m = currentDate.getMonth() - inputDate.getMonth();

    	if (m < 0 || (m === 0 && currentDate.getDate() < inputDate.getDate())) {
    	    age--;
    	}

    	console.log("- age : ",age)
        if (age < 18 || age > 100) {
            $("#dobCheck").text("Age must be between 18 to 100 years").show();
            return false;
        } else {
            $("#dobCheck").hide();
            return true;
        }

    }

    // function to validate password
    function validatePassword() {
    	console.log("validatePassword is called..");
    	let passValue = $("#password").val();
    	console.log(passValue);

    	// regex for valid password format
    	// Password must contain one digit from 1 to 9, one lowercase letter, one uppercase letter, one special character, no space, and it must be 8-16 characters long.
    	let regex = /^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*\W)(?!.* ).{8,20}$/;

       	if (!passValue) {
    		$("#passwordCheck").show();
    		$("#passwordCheck").html("Password is missing");
            return false;
        } else if (!regex.test(passValue)) {
            $("#passwordCheck").show();
            $("#passwordCheck").html("8-20 chars with atleast 1 digit, 1 lowercase, 1 uppercase, 1 special, no spaces.");
            return false;
    	}

        $("#passwordCheck").hide();
        return true;
    }


    // function to validate userid
    function validateUserId() {
    	console.log("validateUserId is called..");
    	let useridValue = $("#userid").val();
    	console.log(useridValue);

    	let regex = /^\d+$/;

       	if (!useridValue) {
    		$("#useridCheck").show();
    		$("#useridCheck").html("UserId is missing");
            return false;
        } else if (!regex.test(useridValue)) {
    	        $("#useridCheck").show();
    	        $("#useridCheck").html("only numbers are allowed");
    	        return false;
    	}

        $("#useridCheck").hide();
        return true;
    }

    // function to validate first name
    function validateCaptcha() {
        console.log("validateCaptcha is called..");
        let captchaValue = $("#captchaTxt").val();
        console.log(captchaValue);

        let regex = /^[A-Za-z0-9]+$/;

        if (!captchaValue) {
            $("#captchaCheck").show();
            $("#captchaCheck").html("CAPTCHA is missing");
            return false;
        } else if (!regex.test(captchaValue) || captchaValue.length !== 5) {
                $("#captchaCheck").show();
                $("#captchaCheck").html("Invalid CAPTCHA");
                $("#captchaTxt").html("");
                return false;
        }

        $("#captchaCheck").hide();
        return true;
    }
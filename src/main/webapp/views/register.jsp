<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="s" uri="/struts-tags" %>

<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
        <title>Register</title>
        <%@include file="header.jsp" %>
        <script>
            $(document).ready(function() {
                $('#reg-link').addClass("active");
                $('#view-users-link').removeClass("active");
                $('#reg-form').trigger("reset");
            });
        </script>
    </head>
    <body>
        <%@include file="./navbar.jsp" %>

        <main class="form-container">
            <s:form action="register" id="reg-form" method="post" theme="simple">
                <s:if test="repeatRequest == true">
                    <script>
                        $(document).ready(function() {
                            isRepeatRequest = true;
                        });
                    </script>
                </s:if>

                <!-- alert message -->
                <s:if test="message != null">
                    <script>
                        $(document).ready(function() {
                            alert('<s:property value="message" />');
                        });
                    </script>
                </s:if>

                <h2>Register</h2>

                <!-- First Name -->
                <div class="input-box">
                    <label for="fname">First Name</label>
                    <s:textfield name="fname" id="fname" placeholder="Enter first name" maxlength="15" required="true" />
                    <h5 id="fnameCheck" class="error"></h5>
                </div>

                <!-- Last Name -->
                <div class="input-box">
                    <label for="lname">Last Name</label>
                    <s:textfield name="lname" id="lname" placeholder="Enter last name" maxlength="15" required="true" />
                    <h5 id="lnameCheck" class="error"></h5>
                </div>

                <!-- Email -->
                <div class="input-box">
                    <label for="email">Email Id</label>
                    <s:textfield name="email" id="email" type="email" placeholder="Enter email address" maxlength="100" required="true" />
                    <h5 id="emailCheck" class="error"></h5>
                </div>

                <!-- DOB -->
                <div class="input-box">
                    <label for="dobPicker">Date of Birth</label>
                    <s:textfield name="dob" id="dobPicker" placeholder="DD/MM/YYYY" readonly="true" required="true" />
                    <h5 id="dobCheck" class="error" style="display:none; color:red;">Age must be between 18 and 100 years</h5>
                </div>

                <!-- GENDER -->
                <div class="input-box">
                    <label>Gender</label>
                    <div class="gender-grp">
                        <input type="radio" class="gender-radio" name="gender" value="Male" checked="checked" style="margin-left:0"/><span>Male</span>
                        <input type="radio" class="gender-radio" name="gender" value="Female"/><span>Female</span>
                        <input type="radio" class="gender-radio" name="gender" value="Other"/><span>Other</span>
                    </div>
                    <h5 id="genderCheck" class="error"></h5>
                </div>

                <!-- Password -->
                <div class="input-box">
                    <label for="password">Password</label>
                    <div class="pass-box">
                        <s:password name="password" id="password" placeholder="Enter password" maxlength="20" required="true" />
                        <span class="show-password-label">
                            <img alt="hide password" class="show-pass-check" id="show-password" src="./images/hide.png">
                        </span>
                    </div>
                    <h5 id="passwordCheck" class="error"></h5>
                </div>

                <!-- Checkbox -->
                <div class="check-box">
                    <input type="checkbox" id="acceptBox" value="y"/>
                    <span>Accept terms and conditions</span>
                </div>

                <div class="input-box captcha-box">
                    <s:textfield name="captchaTxt" id="captchaTxt" placeholder="Enter CAPTCHA" maxlength="5" />
                    <a href="#" id="captchaReset" >
                        <img alt="reset" class="show-pass-check" src="./images/reset.svg">
                    </a>
                    <img src="captcha.action" alt="CAPTCHA" id="captchaImg" />
                    <h5 id="captchaCheck" class="error">CAPTCHA is missing</h5>
                </div>
                <!-- Submit Button -->
                <button type="submit" id="regBtn" class="btn-disabled" disabled="disabled">Register</button>

            </s:form>
        </main>
        <%@include file="./footer.jsp" %>

        <script>
            $('#captchaReset').on('click', function(e) {
                e.preventDefault();
                // Add animation class
                $('#captchaReset img').addClass('rotate-animation');
                // Wait 1 second, then reload the captcha and remove animation class
                setTimeout(function() {
                    $('#captchaImg').attr('src', 'captcha.action?');// + Date.now());   // to bypass browser cache
                    $('#captchaReset img').removeClass('rotate-animation');
                }, 1000);
            });
        </script>
    </body>

</html>


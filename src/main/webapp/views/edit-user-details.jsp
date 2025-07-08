<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
        <title>Register</title>
        <%@include file="header.jsp" %>
    </head>
    <body>
        <jsp:include page="./navbar.jsp"/>

        <main class="form-container">

            <%-- Success Case --%>
            <s:if test="error == false && message != null">
                <script>
                    $(document).ready(function () {
                        alert('<s:property value="message"/>');
                        window.location.href='<s:url value="view-users"/>';
                    });
                </script>
            </s:if>

            <%-- Error Case--%>
            <s:if test="error == true && message != null">
                <script>
                    $(document).ready(function () {
                        alert('<s:property value="message"/>');
                    });
                </script>
            </s:if>


            <s:form action="update-user" id="myform" method="post"  theme="simple">

                <h2>Edit Details</h2>

                <input type="hidden" id="editMode" value="true" />

                <!-- UserId -->
                <s:hidden name="userid" id="userid" value="%{user.userId}" />

                <!-- First Name -->
                <div class="input-box">
                    <label for="fname">First Name</label>
                    <s:textfield name="fname" id="fname" placeholder="Enter first name"
                        value="%{user.firstName}" required="true" />
                    <h5 id="fnameCheck" class="error"></h5>
                </div>

                <!-- Last Name -->
                <div class="input-box">
                    <label for="lname">Last Name</label>
                    <s:textfield name="lname" id="lname" placeholder="Enter last name"
                        value="%{user.lastName}" required="true" />
                    <h5 id="lnameCheck" class="error"></h5>
                </div>

                <!-- Email -->
                <div class="input-box">
                    <label for="email">Email Id</label>
                    <s:textfield name="email" id="email" placeholder="Enter email address" type="email"
                        value="%{user.email}" required="true"/>
                    <h5 id="emailCheck" class="error"></h5>
                </div>

                <!-- DOB -->
                <div class="input-box">
                    <label for="dobPicker">Date of Birth</label>
                    <s:textfield name="dob" id="dobPicker" placeholder="DD/MM/YYYY" value="%{formattedDob}" readonly="true"/>
                    <h5 id="dobCheck" class="error" style="display:none; color:red;"></h5>
                </div>

                <!-- GENDER -->
                <div class="input-box">
                     <label>Gender</label>
                     <div class="gender-grp">
                          <input type="radio" class="gender-radio" name="gender" value="Male"
                              <s:if test="%{user.gender == 77}">checked="checked"</s:if> style="margin-left:0"/><span>Male</span> <!-- For 'M' -->

                          <input type="radio" class="gender-radio" name="gender" value="Female"
                              <s:if test="%{user.gender == 70}">checked="checked"</s:if>/><span>Female</span><!-- For 'F' -->

                          <input type="radio" class="gender-radio" name="gender" value="Other"
                              <s:if test="%{user.gender == 79}">checked="checked"</s:if>/><span>Other</span><!-- For 'O' -->
                    </div>
                    <h5 id="genderCheck" class="error"></h5>
                </div>

                <!-- Submit -->
                <button type="button" id="updtBtn" class="btn-disabled" onclick="checkUpdate()" disabled="disabled">Update</button>
            </s:form>
        </main>
        <%@include file="./footer.jsp" %>
        <script>
            let fnameValue = $("#fname").val();
            let lnameValue = $("#lname").val();
            let emailValue = $("#email").val();
            let dobValue = $("#dobPicker").val();
            let genderValue = $("input[name='gender']:checked").val();
            console.log("genderValue:", genderValue);

            console.log(fnameValue, lnameValue, emailValue, dobValue);
            function checkUpdate() {
                let currFnameValue = $("#fname").val();
                let currLnameValue = $("#lname").val();
                let currEmailValue = $("#email").val();
                let currDobValue = $("#dobPicker").val();
                let currGenderValue = $("input[name='gender']:checked").val();
                console.log('checkUpdate:: ',currFnameValue, currLnameValue, currEmailValue, currDobValue, currGenderValue);
                if (currFnameValue !== fnameValue || currLnameValue !== lnameValue
                        || currEmailValue !== emailValue || currDobValue !== dobValue || currGenderValue  !== genderValue) {
                    $("#myform").submit();
                } else {
                    alert("No changes detected");
                }
            }
        </script>

    </body>
</html>


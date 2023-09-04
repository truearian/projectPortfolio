document.addEventListener("DOMContentLoaded", function () {
    const loginForm = document.getElementById("login-form");
  
    loginForm.addEventListener("submit", function (event) {
      event.preventDefault();
  
      const username = document.getElementById("Login").value;
      const password = document.getElementById("password").value;
  
      const formData = new FormData();
      formData.append("Login", username);
      formData.append("Password", password);
      
      console.log(username);
      console.log(password);
  
      fetch("../LAMPAPI/Login.php", {
        method: "POST",
        body: formData,
      })
        .then((response) => response.json())
        .then((data) => {
          if (data.error === "") {
            console.log("Login successful");
            console.log("User ID:", data.ID);
            console.log("First Name:", data.FirstName);
            console.log("Last Name:", data.LastName);
          } else {
            // Login failed, display error message
            console.error("Login failed:", data.error);
            console.log("User ID:", data.ID);
            console.log("First Name:", data.FirstName);
            console.log("Last Name:", data.LastName);
          }
        })
        .catch((error) => {
          // Handle fetch errors
          console.error("Fetch error:", error);
        });
    });
  });
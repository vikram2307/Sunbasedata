<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<!DOCTYPE html>
<html>
<head>
    <meta charset="ISO-8859-1">
    <title>Customer List</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha3/dist/css/bootstrap.min.css"
        rel="stylesheet"
        integrity="sha384-KK94CHFLLe+nY2dmCWGMq91rCGa5gtU4mk92HdvYe+M/SXH301p5ILy+dN9+nJOZ"
        crossorigin="anonymous">

    <link rel="stylesheet"
        href="https://maxst.icons8.com/vue-static/landings/line-awesome/line-awesome/1.3.0/css/line-awesome.min.css">
    
    <style>
        body {
            background-color: #0e1525; 
        }

        #header {
            background-color: #black; 
            padding: 10px;
            text-align: center;
        }

        #showData {
            margin: 2% 10%;
            background-color: #fff;
            padding: 2% 5%;
            border-radius: 20px;
        }

        #addcustomer {
            margin: 1% 1% 0% 70%;
        }

        #searchBar {
            margin-bottom: 2%;
            display: flex;
            align-items: center;
        }

        #searchForm {
            display: -webkit-inline-box;
            margin-right: 0px;
            flex: 1; 
        }

        #customerList {
            margin-top: 20px; 
        }

        #paginationContainer {
            display: flex;
            gap: 5px;
        }

        #paginationContainer button {
            padding: 5px 10px;
            cursor: pointer;
            border: 1px solid #fff;
            color: #fff;
            background-color: #007bff;
            border-radius: 5px;
        }

        #paginationContainer button.active {
            background-color: #0056b3;
        }
        
        #syncButton {
            margin-bottom: 10px;
        }
    </style>
</head>

<body>
    <div id="header">
        <h2 style="color: #fff;">Customer List</h2>
    </div>
    
    <div id="showData">
        <div>
            <div id="searchBar" class="input-group mb-3">
                <form id="searchForm" action="/search" method="get">
                    <input type="text" name="keyword" class="form-control" placeholder="Search..." aria-label="Search..."
                        aria-describedby="basic-addon2">
                    <select class="form-select" id="searchDropdown" name="criteria">
                        <option selected>Select Criteria</option>
                        <option value="All">All</option>
                        <option value="first_name">First Name</option>
                        <option value="last_name">Last Name</option>
                        <option value="city">City</option>
                        <option value="email">Email</option>
                        <option value="phone">Phone</option>
                    </select>
                    <button class="btn btn-outline-secondary" type="submit">Search</button>
                </form>
                
                <a id="addcustomer" class="btn btn-primary btn-md" href="addcustomer">Add Customer</a>
            </div>
        </div>
        
        <div>
            <a id="syncButton" class="btn btn-primary btn-md" href="syncData">Sync Data <i class="las la-sync"></i></a>
        </div>  

        <table class="table" id="customerList">
            <thead>
                <tr>
                    <th scope="col">ID</th>
                    <th scope="col">First Name</th>
                    <th scope="col">Last Name</th>
                    <th scope="col">Street</th>
                    <th scope="col">Address</th>
                    <th scope="col">City</th>
                    <th scope="col">State</th>
                    <th scope="col">Email</th>
                    <th scope="col">Phone</th>
                    <th scope="col">Action</th>
                </tr>
            </thead>
            <tbody>
                <c:forEach items="${list}" var="u">
                    <tr>
                        <td>${u.id}</td>
                        <td>${u.first_name}</td>
                        <td>${u.last_name}</td>
                        <td>${u.street }</td>
                        <td>${u.address }</td>
                        <td>${u.city }</td>
                        <td>${u.state}</td>
                        <td>${u.email}</td>
                        <td>${u.phone}</td>
                        <td>
                            <a href="update/${u.id}" class="btn btn-success btn-md">Edit <i class="las la-edit"></i></a>
                            <a href="delete/${u.id}" class="btn btn-danger btn-md">Delete <i class="las la-trash-alt"></i></a>
                        </td>
                    </tr>
                </c:forEach>
            </tbody>
        </table>
        
        <!-- Pagination Container -->
        <div id="paginationContainer" class="mt-3"></div>
    </div>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha3/dist/js/bootstrap.bundle.min.js"
        integrity="sha384-ENjdO4Dr2bkBIFxQpeoTz1HIcje39Wm4jDKdf19U8gI4ddQ3GYNS7NTKfAdVQSZe"
        crossorigin="anonymous"></script>
    <!-- JavaScript for Pagination -->
<script>
    document.addEventListener("DOMContentLoaded", function () {
        const table = document.getElementById("customerList");
        const rows = table.getElementsByTagName("tbody")[0].getElementsByTagName("tr");
        const rowsPerPage = 5; // Number of rows per page
        let currentPage = 1;

        function showPage(page) {
            for (let i = 0; i < rows.length; i++) {
                rows[i].style.display = (i >= (page - 1) * rowsPerPage && i < page * rowsPerPage) ? "" : "none";
            }
        }

        function updatePaginationButtons() {
            const totalPages = Math.ceil(rows.length / rowsPerPage);

            // Update the pagination buttons
            const paginationContainer = document.getElementById("paginationContainer");
            paginationContainer.innerHTML = "";

            for (let i = 1; i <= totalPages; i++) {
                const button = document.createElement("button");
                button.innerText = i;
                button.addEventListener("click", function () {
                    currentPage = i;
                    showPage(currentPage);
                    updatePaginationButtons();
                });
                if (i === currentPage) {
                    button.classList.add("active");
                }
                paginationContainer.appendChild(button);
            }
        }

        showPage(currentPage);
        updatePaginationButtons();
    });
</script>

</body>
</html>

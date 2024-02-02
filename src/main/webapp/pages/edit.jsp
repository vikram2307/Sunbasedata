<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="ISO-8859-1">
    <title>Edit Customer</title>
    <link rel="stylesheet"
        href="https://cdn.jsdelivr.net/npm/bootstrap@4.0.0/dist/css/bootstrap.min.css"
        integrity="sha384-Gn5384xqQ1aoWXA+058RXPxPg6fy4IWvTNh0E263XmFcJlSAwiGgFAW/dAiS6JXm"
        crossorigin="anonymous">
</head>
<style>
    body {
        background-color: #0e1525; 
        color: #fff;
    }

    form {
        max-width: 500px;
        margin: 5% auto;
        padding: 20px;
        border-radius: 15px;
        background-color: #040828;
        box-shadow: 0 0 10px rgba(0, 0, 0, 0.2);
    }

    h1 {
        text-align: center;
        margin-bottom: 30px;
    }

    label {
        font-size: 18px;
    }

    .form-control {
        border-radius: 8px;
        background-color: #fff; 
        color: #000; 
    }

    .btn-primary {
        background-color: #007bff;
        border-color: #007bff;
        border-radius: 8px;
        padding: 10px 30px;
    }
</style>
<body>
    <center>
        <h1>
            <label id="label" for="name" class="form-label">Edit Customer</label>
        </h1>
    </center>
    
    <form action="edit" method="post">
        <input type="hidden" name="id" value="${customer.id}" />
        
        <div class="form-row">
            <div class="form-group col-md-6">
                <label for="name" class="form-label">First Name</label>
                <input type="text" name="first_name" class="form-control" value="${customer.first_name}" required/>
            </div>
            <div class="form-group col-md-6">
                <label for="email" class="form-label">Last Name</label>
                <input type="text" name="last_name" class="form-control" value="${customer.last_name}" required/>
            </div>
        </div>

        <div class="form-row">
            <div class="form-group col-md-6">
                <label for="password" class="form-label">Street</label>
                <input type="text" name="street" class="form-control" value="${customer.street}" required/>
            </div>
            <div class="form-group col-md-6">
                <label for="conpassword" class="form-label">Address</label>
                <input type="text" name="address" class="form-control" value="${customer.address}" required/>
            </div>
        </div>

        <div class="form-row">
            <div class="form-group col-md-6">
                <label for="email" class="form-label">City</label>
                <input type="text" name="city" class="form-control" value="${customer.city}" required/>
            </div>
            <div class="form-group col-md-6">
                <label for="email" class="form-label">State</label>
                <input type="text" name="state" class="form-control" value="${customer.state}" required/>
            </div>
        </div>

        <div class="form-row">
            <div class="form-group col-md-6">
                <label for="email" class="form-label">Email</label>
                <input type="text" name="email" class="form-control" value="${customer.email}" required/>
            </div>
            <div class="form-group col-md-6">
                <label for="email" class="form-label">Phone</label>
                <input type="text" name="phone" class="form-control" value="${customer.phone}" required/>
            </div>
        </div>

        <div class="mb-3">
            <input type="submit" class="btn btn-primary" value="Save" />
        </div>
    </form>

    <script src="https://code.jquery.com/jquery-3.2.1.slim.min.js"
        integrity="sha384-KJ3o2DKtIkvYIK3UENzmM7KCkRr/rE9/Qpg6aAZGJwFDMVNA/GpGFF93hXpG5KkN"
        crossorigin="anonymous"></script>
    <script
        src="https://cdn.jsdelivr.net/npm/popper.js@1.12.9/dist/umd/popper.min.js"
        integrity="sha384-ApNbgh9B+Y1QKtv3Rn7W3mgPxhU9K/ScQsAP7hUibX39j7fakFPskvXusvfa0b4Q"
        crossorigin="anonymous"></script>
    <script
        src="https://cdn.jsdelivr.net/npm/bootstrap@4.0.0/dist/js/bootstrap.min.js"
        integrity="sha384-JZR6Spejh4U02d8jOt6vLEHfe/JQGiRRSQQxSfFWpi1MquVdAyjUar5+76PVCmYl"
        crossorigin="anonymous"></script>
</body>
</html>

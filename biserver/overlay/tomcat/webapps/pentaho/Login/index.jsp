<%@ page pageEncoding="UTF-8" %>
<%@ page language="java" %>
<!doctype html>
<html lang="en">

<head>
	<meta charset="utf-8">
	<meta name="viewport" content="width=device-width, initial-scale=1">
	<title>Login</title>
</head>

<body>
	<form method="post" action="../j_spring_security_check">
		<table>
			<tr>
				<td><label>Username</label></td>
				<td><input name="j_username" value="admin" type="text" autocomplete="off"></td>
			</tr>
			<tr>
				<td><label>Password</label></td>
				<td><input name="j_password" type="password" value="password" autocomplete="off"></td>
			</tr>
			<tr>
				<td colspan="2"><input type="submit" value="Login"></td>
			</tr>
		</table>
	</form>
</body>

</html>

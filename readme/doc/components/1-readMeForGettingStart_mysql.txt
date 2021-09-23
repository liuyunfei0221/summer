1.安装mysql并配置mysql8的加密方式,如下:


2.打开mysql命令行窗口.

3.使用mysql -u root -p命令登录mysql.

4.使用以下命令修改认证方式,请自行替换用户名,访问域及密码等相关内容.

    下面以本机开发环境为例,请根据需要自行修改:

	use mysql;
	修改加密方式:	ALTER USER'root'@'localhost' IDENTIFIED BY 'password' PASSWORD EXPIRE NEVER;
	修改用户密码:	ALTER USER'root'@'localhost' IDENTIFIED WITH mysql_native_password BY 'password';
	刷新权限:		FLUSH PRIVILEGES;
	使用navicat等相关工具测试连接。


5.请自行修改yml中相关的数据库配置
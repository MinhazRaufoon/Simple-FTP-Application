<html><body>
<h1>Simple FTP Application</h1>
<h3>Description</h3>
<p>This project implements a simple FTP application using the TCP protocol which offers file transfer among the server and clients. There are two extensions- <ul><li>Server</li><li>Client</li></ul> </p>

<h3>Prerequisites</h3>
<ul><li>Java version 1.7.0_51</li></ul>

<h3>Server</h3>
<h5>Features</h5>
 <ul>
 <li>Sharing the files under a directory in server computer</li>
 <li>Handling multiple clients' connection requests, uploads and downloads</li>
 <li>Supports anonymous clients</li>
 </ul><br>
<h5>How to use</h5>
<p>Run the ServerMain.java. The following view will appear</p><br>
<img src="http://i.imgur.com/rW0CWGV.png" width="700" height="350">
<p>To start a server, input the path of the directory you want to share in the 'Other Settings' and click on 'Connect Server'. In the dialog, input 'localhost' in the 'Server Address'. Then the following will be the changed view</p><br>
<img src="http://i.imgur.com/5wpN7ck.png" width="700" height="350">
<p>When a client becomes connected to the server, a message is shown in the 'Message Log'. Same thing goes for disconnecting.</p><br>
<img src="http://i.imgur.com/ytATq7u.png" width="600" height="350">
<p>When a client is downloading or uploading files, those will appear in the 'Transfer List' section. Also a message will appear at the 'Message Log'</p><br>
<img src="http://i.imgur.com/NNxtBaZ.png" width="700" height="350">


<h3>Client</h3>
<h5>Features</h5>
 <ul>
 <li>Connecting to a server</li>
 <li>Upload and download files</li>
 <li>Pause and resume on the fly</li>
 <li>Browse clients' hard disk</li>
 <li>Browse Server's shared directory</li>
 </ul><br>
<h5>How to use</h5>
<p>Run the ClientMain.java. Click load server. Input the following and click ok. You will be connected to that server</p><br>
<img src="http://i.imgur.com/HBEz62X.png" width="700" height="350">
<p>You can browse your hard disk at the left side. After getting connected, you will be able to view the server's directory files at the right section.</p><br>
<img src="http://i.imgur.com/7ZeJeT5.png" width="700" height="350">
<p>To download or upload, select a folder at the bottom right or bottom left sections and click on the respective buttons. The downloads and uploads will appear at the upper-right corner</p><br>
<img src="http://i.imgur.com/mbhsg4w.png" width="700" height="350">

<h3>Limitations</h3>

<h5>Features not implemented</h5>
Server side:<br>
<ul>
<li>Registered clients</li>
<li>Advantages for registered clients</li>
<li>Directory transfer</li>
</ul>
Client side:<br>
<ul>
<li>Directory transfer</li>
<li>Registration process</li>
<li>bookmarks</li>
</ul>
<h5>Limitations in implemented features</h5>
<ul>
<li>Doesn't work smooth with lower java versions</li>
<li>Lacks a smooth scheduling algorithm</li>
<li>Memory and CPU usage not optimized</li>
<li>Not suitable for rough use, as there are many exceptions which are not handled</li>
</ul>

<h3>Conclusion</h3>
<p>I have developed it within a short period of time for the project of Computer Networking course. And there are so many features yet to be implemented which I plan to do in future.</p>
</body></html>

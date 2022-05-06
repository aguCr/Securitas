# Securitas

-mobile application development 1 project (3 semester) made in a team of 2 persons-<br>

Securitas is a simple password manager<br>

<nav>
<ol>
<li><a href="#1"> Description</a></li>
<li><a href="#2"> Motivation</a></li>
<li><a href="#3"> Features</a></li>
<li><a href="#4"> Possible future updates</a></li>
<li><a href="#5"> Used Design Patterns</a></li>
</ol>
</nav>

<div>
<h2 id="1">Description</h2>
Securitas is a simple password manager. It allows you to save passwords and add some 
additional information to them, like a title, category or some notes depending on the use case.
The application has an integrated password generation function to provide the possibility to create
passwords in a quick, easy and secure way. The app checks the quality of a password.<br>
We defined a secure password with 5 features:
<ul>
<li>a minimal length of 8 characters</li>
<li>it contains at least one lower case character</li>
<li>it contains at least one upper case character</li>
<li>it contains at least one number</li>
<li>it contains at least one special symbol</li>
</ul>
So a strong(green) password has 5 features, a password with 3 or 4 features is ok(orange) and everything below is weak(red).
</div> 

<div>
<h2 id="2">Motivation</h2>
Nowadays you need to register on a lot of websites to be able to use all features, even if you
do not want to save any data to your account. Your I-cloud, G-mail or business E-Mail is too valuable
so that you create a Spam-Mail, but when the time passes, even this E-Mail becomes valuable
so you accumulate a lot of different e-mails with different passwords during the time.
And that is not the only problem, because you mostly use another password for the website you log into.<br>
It is very annoying if you forget which e-mail you used or what the password was. This is why we wanted to make a password
manager that is easy to use and which we can trust because we know the data is not sent or read 
from someone else (because we coded it ourselves).<br>
The second motivation for the password-generator was the the digitization.
Nowadays the smartphone has become an important helper. For your banking account or
personal data you should use a strong password. But the most of the people do not really
care about this or do not want to take time to invent and memorize a secure password and use simple passwords like their birth date, names of their kids or pets.
That is why we wanted to provide a simple way to make secure passwords without any effort.
</div>

<div>
<h2 id="3">Features</h2>
<ul>
<li>3lvl-Quality checker (strong/ ok/ weak)</li>
<li>password generator</li>
<li>biometric authentication or PIN if you do not use/ have a fingerprint-sensor</li>
</ul>
Minor features:
<ul>
<li>darkmode</li>
<li>you can save a password only if the required fields(title, password) are filled</li>
<li>By default, the generator generates strong passwords</li>
<li>the generated password is automatically added to the password text field</li>
<li>Generator only uses selected characters:
<ul>
<li>upper cases: &nbsp;&nbsp &nbsp; A B C D E F G H I J K L M N O P Q R S T U V W X Y Z </li>
<li>lower cases: &nbsp;&nbsp;&nbsp;&nbsp;&nbsp; a b c d e f g h i j k l m n o p q r s t u v w x y z  </li>
<li>numbers: &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;  1 2 3 4 5 6 7 8 9 0</li>
<li>special symbols: ! § $ % ? @ . - _ ~ + * € & </li>
</ul>
</li>
<li>generate button disables if you deselect all chars</li>
<li>radio button to set the first char is being disabled if you deselect the corresponding char property</li>
<li>by tipping the enter key you move to the next edittext until you reach the notes</li>
<li>swiping to the left in the list activity deletes the password</li>
<li>you can undo this for the next few seconds</li>
<li>passwords can be edited</li>
<li>long pressing on a password copies it to the clipboard, to reuse it easily</li>
<li>if the title of a password is too long it will be ellipsized (e.g. myLongPaswor...)</li>
<li>the app does not allow screenshots or recordings of the password list</li>
</ul>
</div>

<div>
<h2 id="4">Possible future updates</h2>
Due to time issues and the lack of knowledge/ experience there were some features we could not implement.
In the future we could imagine to add them to our application.
<ul>
<li>
By swiping to delete we would like to add a trash bin icon and red color to indicate, that the swipe deletes the password
</li>
<li>search function if you have a lot of passwords</li>
<li>bookmarks for the most used passwords</li>
<li>a filter by category (for now the category is redundant)</li>
<li>drag and drop the rearrange the order of the displayed passwords</li>
<li>clicking on enter on the notes text field creates a new line (because it is a multi line field) instead of leaving the text field, so you have to close the keyboard manually. We need to improve this workflow</li>
<li>show the password quality while typing an own password (quality is indicated after it was saved with the color)</li>
</ul>
</div>

<div>
<h2 id="5">Used Design Patterns</h2>
<ul>
<li>MVVM to separate business logic from ui and make the app lifecycle aware (survives rotation, switch to dark mode etc.)</li>
<li>Adapter Pattern for the RecyclerViews</li>
<li>Singleton Pattern to instantiate the database only one time/li>
<li>Observer Pattern (password is being observed) with the shared ViewModel to paste it after the generation to the corresponding input field</li>
</ul>
</div>





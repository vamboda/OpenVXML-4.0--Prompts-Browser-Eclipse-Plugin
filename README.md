
This project is eclipse plug-in for eclipse voice tools project called OpenVXML. OpenVXML is a fully functional Voice Application design and development tool. Based on a drag and drop interface the goal of the voice tools project is to help lower the curve for IVR voiceXML development and utilize the Eclipse license to give everyone the opportunity to participate. It is a runtime based development tool.

I am interested in OpenVXML, i like to mess with it, how?
They have pretty good documentation. Start right here: http://www.eclipse.org/vtp/documentation/getting-started/UsersGuide.php

What is the current stable version?
Current stable version is at version 3.0
Enhanced version is version 4.0 . This plug is only for openVXML 4.0 which is an advanced version. Please view this link for installing openVXML 4.0
http://www.eclipse.org/forums/index.php/t/217010/

What's the use of this eclipse plug-in or prompts view or what ever?
This plug-in is called prompts view or prompts browser. In OpenVXML environment, you will play with drag & drop widgets to create voice application. And then configure these widgets with various settings like grammars, prompts, DTMF etc… Lets call these widgets as Modules.  This plug-in exposes all the media configurations to the media modules like following :
	• PlayPrompt 	: A PlayPrompt is the most basic unit of the IVR – it simply cues up audio to be played by the voice browser. This can be TTS, pre-recorded audio, or dynamic combinations of prerecorded audio snippets to play dynamic data
	• Question 	: A Question module is used when you would like to ask something simple of the user, not present them with several options that all require their own prompts and grammars. This could be a yes or no, a phone number, desired pizza toppings, etc.
	• OptionSet 	: An OptionSet module provides the menu of options to the caller (along with the associated prompts and grammars for each option), and results in a branch, with one path for each option and then two more paths to handle caller errors. 
	• Record	:When you wish to record a Caller's input verbatim instead of using speech recognition or checking for a DTMF value, you need a Record Module. This records the caller's speech into an audio file that can be archived, transcribed, emailed, etc.
This plug-in deals with the only above  modules. There are various types of media settings to these modules. An OpenVMXL Voice Project contains large number of media modules as compared to other ones. As developer is human*, there is a 100% chance that he will miss out loads of the configurations to these modules or may miss out to change the default configurations. Since tester is also human, he may not test the application thoroughly (covering each path & each module & each setting which can be a grammar/ prompt/ DTMF) and due to which application can be a disaster in production environment. 

How to Play with this Plug-in?
Get the jay from 'dist ' folder and drop it in the 'eclipse/plugins' folder of  [OpenVXML] eclipse.

*
Human:The smartest creature in the world that still acts like a dumb animal from time to time.
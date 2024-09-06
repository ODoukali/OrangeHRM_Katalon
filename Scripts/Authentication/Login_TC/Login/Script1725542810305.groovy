import static com.kms.katalon.core.checkpoint.CheckpointFactory.findCheckpoint
import static com.kms.katalon.core.testcase.TestCaseFactory.findTestCase
import static com.kms.katalon.core.testdata.TestDataFactory.findTestData
import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject
import static com.kms.katalon.core.testobject.ObjectRepository.findWindowsObject
import com.kms.katalon.core.checkpoint.Checkpoint as Checkpoint
import com.kms.katalon.core.cucumber.keyword.CucumberBuiltinKeywords as CucumberKW
import com.kms.katalon.core.mobile.keyword.MobileBuiltInKeywords as Mobile
import com.kms.katalon.core.model.FailureHandling as FailureHandling
import com.kms.katalon.core.testcase.TestCase as TestCase
import com.kms.katalon.core.testdata.TestData as TestData
import com.kms.katalon.core.testng.keyword.TestNGBuiltinKeywords as TestNGKW
import com.kms.katalon.core.testobject.TestObject as TestObject
import com.kms.katalon.core.webservice.keyword.WSBuiltInKeywords as WS
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import com.kms.katalon.core.windows.keyword.WindowsBuiltinKeywords as Windows
import internal.GlobalVariable as GlobalVariable
import org.openqa.selenium.Keys as Keys
import groovy.json.JsonSlurper
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import java.nio.file.Files
import java.nio.file.Paths

// Load JSON file
// Load JSON file
def slurper = new JsonSlurper()
File f = new File("C:/Users/odoukali/Desktop/DevOps/OrangeHRM/OrangeHRM/Data Files/Users/user_data.json")
def inputJSON = slurper.parse(f)

// Open browser and navigate to the login page once
WebUI.openBrowser('')
WebUI.navigateToUrl('https://opensource-demo.orangehrmlive.com/web/index.php/auth/login')

// Loop through the JSON and extract username/password
inputJSON.each { userData ->
    String username = userData.username
    String password = userData.password

    // Set the username and password in the login form
    WebUI.setText(findTestObject('Object Repository/Page_OrangeHRM/input_Username_username'), username)
    WebUI.setText(findTestObject('Object Repository/Page_OrangeHRM/input_Username_oxd-input oxd-input--focus_1'), password)

    // Click on the login button
    WebUI.click(findTestObject('Object Repository/Page_OrangeHRM/button_Login'))
    WebUI.delay(1)

    // Check if login is successful or if there's an error
    if (WebUI.verifyElementPresent(findTestObject('Object Repository/Page_Dashboard/dashboardElement'), 5, FailureHandling.OPTIONAL)) {
        // If login is successful
        println "Login successful for user: " + username
        WebUI.verifyElementText(findTestObject('Object Repository/Page_Dashboard/welcomeMessage'), 'Welcome')

        // Perform logout after successful login, but don't close the browser
        WebUI.click(findTestObject('Object Repository/Page_OrangeHRM/a_Logout'))

        // Navigate back to login page for next attempt
        WebUI.navigateToUrl('https://opensource-demo.orangehrmlive.com/web/index.php/auth/login')
    } else if (WebUI.verifyElementPresent(findTestObject('Object Repository/Page_Login/errorMessage'), 5, FailureHandling.OPTIONAL)) {
        // If there's an error message (login failed)
        println "Login failed for user: " + username + " - Invalid credentials."
    }
}

// Close the browser after all login attempts
WebUI.closeBrowser()



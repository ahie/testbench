/**
 * Copyright (C) 2012 Vaadin Ltd
 *
 * This program is available under Commercial Vaadin Add-On License 2.0
 * (CVALv2) or GNU Affero General Public License (version 3 or later at
 * your option).
 *
 * See the file licensing.txt distributed with this software for more
 * information about licensing.
 *
 * You should have received a copy of the license along with this program.
 * If not, see <http://vaadin.com/license/cval-2.0> or
 * <http://www.gnu.org/licenses> respectively.
 */
package com.vaadin.testbench;

import static org.easymock.EasyMock.anyObject;
import static org.easymock.EasyMock.contains;
import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.createNiceMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.expectLastCall;
import static org.easymock.EasyMock.isA;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Ignore;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriver.Navigation;
import org.openqa.selenium.WebDriver.Options;
import org.openqa.selenium.WebDriver.TargetLocator;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.internal.WrapsDriver;

import com.vaadin.testbench.commands.TestBenchCommands;
import com.vaadin.testbench.commands.TestBenchElementCommands;

public class TestBenchDriverTest {

    @Test
    public void testTestBenchDriverIsAWebDriver() {
        WebDriver driver = TestBench
                .createDriver(createNiceMock(WebDriver.class));
        assertTrue(driver instanceof WebDriver);
    }

    @Test
    public void testTestBenchDriverActsAsProxy() {
        WebDriver mockDriver = createMock(FirefoxDriver.class);
        mockDriver.close();
        expectLastCall().once();
        WebElement mockElement = createNiceMock(WebElement.class);
        expect(mockDriver.findElement(isA(By.class))).andReturn(mockElement);
        List<WebElement> elements = Arrays.asList(mockElement);
        expect(mockDriver.findElements(isA(By.class))).andReturn(elements);
        mockDriver.get("foo");
        expectLastCall().once();
        expect(mockDriver.getCurrentUrl()).andReturn("foo");
        expect(mockDriver.getPageSource()).andReturn("<html></html>");
        expect(mockDriver.getTitle()).andReturn("bar");
        expect(mockDriver.getWindowHandle()).andReturn("baz");
        Set<String> handles = new HashSet<String>();
        expect(mockDriver.getWindowHandles()).andReturn(handles);
        Options mockOptions = createNiceMock(Options.class);
        expect(mockDriver.manage()).andReturn(mockOptions);
        Navigation mockNavigation = createNiceMock(Navigation.class);
        expect(mockDriver.navigate()).andReturn(mockNavigation);
        mockDriver.quit();
        expectLastCall().once();
        expect(
                ((JavascriptExecutor) mockDriver)
                        .executeScript(anyObject(String.class))).andStubReturn(
                true);
        TargetLocator mockTargetLocator = createNiceMock(TargetLocator.class);
        expect(mockDriver.switchTo()).andReturn(mockTargetLocator);
        replay(mockDriver);

        // TestBenchDriverProxy driver = new TestBenchDriverProxy(mockDriver);
        WebDriver driver = TestBench.createDriver(mockDriver);
        driver.close();
        By mockBy = createNiceMock(By.class);
        assertTrue(driver.findElement(mockBy) instanceof TestBenchElementCommands);
        assertTrue(driver.findElements(mockBy).get(0) instanceof TestBenchElementCommands);
        driver.get("foo");
        assertEquals("foo", driver.getCurrentUrl());
        assertEquals("<html></html>", driver.getPageSource());
        assertEquals("bar", driver.getTitle());
        assertEquals("baz", driver.getWindowHandle());
        assertEquals(handles, driver.getWindowHandles());
        assertEquals(mockOptions, driver.manage());
        assertEquals(mockNavigation, driver.navigate());
        driver.quit();
        assertEquals(mockTargetLocator, driver.switchTo());

        verify(mockDriver);
    }

    @Ignore("This opens a web browser window, so we (currently) shouldn't try to run it in any CI environment")
    @Test
    public void testAugmentedDriver() {
        WebDriver driver = TestBench.createDriver(new FirefoxDriver());
        assertTrue(driver instanceof TakesScreenshot);
        driver.close();
    }

    @Test
    public void getWrappedDriver_returnsTheWrappedDriver() {
        WebDriver driverMock = createNiceMock(WebDriver.class);
        WebDriver driver = TestBench.createDriver(driverMock);
        WebDriver wrappedDriver = ((WrapsDriver) driver).getWrappedDriver();
        assertEquals(driverMock, wrappedDriver);
    }

    @Test
    public void testDisableWaitForVaadin() {
        FirefoxDriver mockFF = createMock(FirefoxDriver.class);
        expect(mockFF.executeScript(contains("clients[client].isActive()")))
                .andReturn(true).once();
        WebElement mockElement = createNiceMock(WebElement.class);
        expect(mockFF.findElement(isA(By.class))).andReturn(mockElement);
        replay(mockFF, mockElement);

        WebDriver driver = TestBench.createDriver(mockFF);
        TestBenchCommands tb = (TestBenchCommands) driver;
        tb.disableWaitForVaadin();
        WebElement testBenchElement = driver.findElement(By.id("foo"));

        ((TestBenchElementCommands) testBenchElement).closeNotification();
        tb.enableWaitForVaadin();
        ((TestBenchElementCommands) testBenchElement).closeNotification();

        verify(mockFF, mockElement);
    }

}
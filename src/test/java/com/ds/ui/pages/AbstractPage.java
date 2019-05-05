package com.ds.ui.pages;

import org.openqa.selenium.By;

public abstract class AbstractPage {

    public By title = By.xpath("//div[@class='page-header']/h1");
    public abstract void initialPageLoad();
}
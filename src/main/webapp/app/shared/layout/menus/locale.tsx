import React from 'react';
import { locales, languages } from 'app/config/translation';
import { Dropdown, DropdownMenu, DropdownToggle, DropdownItem } from 'mdbreact';

export const LocaleMenu = ({ currentLocale, onClick }: { currentLocale: string; onClick: (event: any) => void }) =>
  Object.keys(languages).length > 1 ? (
    <Dropdown className="d-flex">
      <DropdownToggle tag="language" className="nav-link text-reset dropdown-toggle me-3 hidden-arrow">
        <i className={currentLocale ? languages[currentLocale].flag : 'america flag m-0'}></i>
      </DropdownToggle>
      <DropdownMenu>
        {locales.map(locale => (
          <DropdownItem key={locale} value={locale} onClick={onClick}>
            <i className={languages[locale].flag}></i>
          </DropdownItem>
        ))}
      </DropdownMenu>
    </Dropdown>
  ) : null;

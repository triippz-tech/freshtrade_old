import React from 'react';
import { locales, languages } from 'app/config/translation';
import { Dropdown, DropdownMenu, DropdownToggle, DropdownItem } from 'mdbreact';
import { findFlag } from 'app/shared/util/flag-utils';

export const LocaleMenu = ({ currentLocale, onClick }: { currentLocale: string; onClick: (event: any) => void }) =>
  Object.keys(languages).length > 1 ? (
    <Dropdown className="d-flex">
      <DropdownToggle tag="language" className="nav-link text-reset dropdown-toggle me-3 hidden-arrow">
        {`${currentLocale.toUpperCase()} ${findFlag(currentLocale)}`}
      </DropdownToggle>
      <DropdownMenu>
        {locales.map(locale => (
          <DropdownItem key={locale} value={locale} onClick={onClick}>
            {`${locale.toUpperCase()} ${findFlag(locale)}`}
          </DropdownItem>
        ))}
      </DropdownMenu>
    </Dropdown>
  ) : null;

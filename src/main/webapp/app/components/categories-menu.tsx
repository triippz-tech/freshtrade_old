import React from 'react';
import { Collapse } from 'reactstrap';

import categories from 'app/shared/data/categoriesmenu.json';
import { Link } from 'react-router-dom';
import Icon from 'app/components/icon';

const CategoriesMenu = ({ long = undefined }) => {
  const [collapse, setCollapse] = React.useState({ [categories.categories[0].name]: true });

  const toggleCollapse = name => {
    setCollapse({ ...collapse, [name]: !collapse[name] });
  };

  const categoriesArray = long ? categories.categories : categories.categories.slice(0, 3);

  return (
    <div className="sidebar-block px-3 px-lg-0">
      <a className="d-lg-none block-toggler" aria-expanded={collapse[categories.name]} onClick={() => toggleCollapse(categories.name)}>
        {categories.name}
        <span className="block-toggler-icon" />
      </a>
      <Collapse isOpen={collapse[categories.name]} className="expand-lg">
        <h5 className="sidebar-heading d-none d-lg-block">{categories.subtitle}</h5>
        <div className="sidebar-icon-menu mt-4 mt-lg-0">
          {categoriesArray.map(category => (
            <div
              key={category.name}
              className="sidebar-icon-menu-item active"
              data-toggle="collapse"
              aria-expanded={collapse[category.name]}
              onClick={() => toggleCollapse(category.name)}
            >
              <div className="d-flex align-items-center">
                <Icon icon={category.icon} className="sidebar-icon" />
                <a className="sidebar-icon-menu-link font-weight-bold mr-2">{category.name}</a>
                <span className="sidebar-icon-menu-count"> {category.productcount}</span>
              </div>
              <Collapse isOpen={collapse[category.name]}>
                <ul className="sidebar-icon-menu sidebar-icon-submenu">
                  {category.subcategories.map((subcategory, index) => (
                    <li key={index} className="sidebar-icon-submenu-item">
                      <Link to={subcategory.link}>
                        <a className="sidebar-icon-submenu-link link-animated link-animated-light">{subcategory.name}</a>
                      </Link>
                    </li>
                  ))}
                </ul>
              </Collapse>
            </div>
          ))}
        </div>
      </Collapse>
    </div>
  );
};

export default CategoriesMenu;

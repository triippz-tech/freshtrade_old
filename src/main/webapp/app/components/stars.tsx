import React from 'react';

const Stars = props => {
  const starsArray = [];
  for (let i = 1; i <= 5; i++) {
    i <= props.stars
      ? starsArray.push(
          <i
            key={i}
            className={`fa fa-star ${props.color ? 'text-' + props.color : 'text-primary'} ${props.starClass ? props.starClass : ''}`}
          />
        )
      : starsArray.push(
          <i
            key={i}
            className={`fa fa-star text-${props.secondColor ? props.secondColor : 'muted'} ${props.starClass ? props.starClass : ''}`}
          />
        );
  }
  return <div className={props.className}>{starsArray}</div>;
};

export default Stars;

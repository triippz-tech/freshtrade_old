import React from 'react';
import Slider from 'rc-slider/lib/Slider';
import 'rc-slider/assets/index.css';

export const DistanceSlider = ({ value, onDistanceChange, startingValue = 25, distanceType = 'mi' }) => {
  const marks = {
    25: `25 ${distanceType}`,
    50: `50 ${distanceType}`,
    75: `75 ${distanceType}`,
    100: `100 ${distanceType}`,
  };

  return (
    <div className="w-100">
      <Slider
        className="w-75 m-auto"
        min={25}
        marks={marks}
        step={25}
        onChange={onDistanceChange}
        value={value}
        defaultValue={startingValue}
        dots
        trackStyle={{
          backgroundColor: '#24a344',
        }}
        handleStyle={{
          borderColor: '#24a344',
          backgroundColor: '#24a344',
        }}
        railStyle={{
          backgroundColor: 'grey',
        }}
      />
    </div>
  );
};

export default DistanceSlider;

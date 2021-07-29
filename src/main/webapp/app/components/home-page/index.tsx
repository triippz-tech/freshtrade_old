import React, { useState } from 'react';
import { Link } from 'react-router-dom';
import { Carousel, CarouselItem, CarouselControl, CarouselIndicators, CarouselCaption } from 'reactstrap';

export interface CItem {
  src: string;
  altText: string;
  caption: string;
  link: string;
  id: string;
}

interface CarouselProps {
  items: Array<CItem>;
}
export const HomePageCarousel = (props: CarouselProps) => {
  const [activeIndex, setActiveIndex] = useState(0);
  const [animating, setAnimating] = useState(false);

  const next = () => {
    if (animating) return;
    const nextIndex = activeIndex === props.items.length - 1 ? 0 : activeIndex + 1;
    setActiveIndex(nextIndex);
  };

  const previous = () => {
    if (animating) return;
    const nextIndex = activeIndex === 0 ? props.items.length - 1 : activeIndex - 1;
    setActiveIndex(nextIndex);
  };

  const goToIndex = newIndex => {
    if (animating) return;
    setActiveIndex(newIndex);
  };

  const slides = props.items.map(item => {
    return (
      <CarouselItem
        onExiting={() => setAnimating(true)}
        onExited={() => setAnimating(false)}
        key={item.id}
        style={{ backgroundColor: 'green' }}
      >
        <Link to={item.link}>
          <img src={item.src} alt={item.altText} height={300} />
          <CarouselCaption captionText={item.caption} captionHeader={item.caption} />
        </Link>
      </CarouselItem>
    );
  });

  return (
    <Carousel activeIndex={activeIndex} next={next} previous={previous} style={{ backgroundColor: 'green' }}>
      <CarouselIndicators items={props.items} activeIndex={activeIndex} onClickHandler={goToIndex} />
      {slides}
      <CarouselControl direction="prev" directionText="Previous" onClickHandler={previous} />
      <CarouselControl direction="next" directionText="Next" onClickHandler={next} />
    </Carousel>
  );
};

import React, { useState } from 'react';
import { Card, CardBody, CardImg, CardText, CardTitle, Carousel, CarouselControl, CarouselIndicators, CarouselItem } from 'reactstrap';
import { TopSellingItemsDTO } from 'app/shared/model/metrics/top-selling-items.dto';
import { IItem } from 'app/shared/model/item.model';

interface TopSellingCardProps {
  items: Array<TopSellingItemsDTO>;
}

const TopSellingCard = (props: TopSellingCardProps) => {
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

  const getImageUrl = (item: IItem) => {
    for (let i = 0; i < item.images.length; i++) {
      if (item.images[i].isVisible) return item.images[i].imageUrl;
    }
    return 'content/images/logo-gray.png';
  };

  const slides = props.items.map(item => {
    return (
      <CarouselItem onExiting={() => setAnimating(true)} onExited={() => setAnimating(false)} key={item.item.id}>
        <Card>
          <CardImg src={getImageUrl(item.item)} alt={item.item.name} top width="100%" height="400px" />
          <CardBody>
            <CardTitle tag="h5">
              <strong>{item.item.name}</strong>
            </CardTitle>
            <CardText>{item.item.details}</CardText>
          </CardBody>
        </Card>
      </CarouselItem>
    );
  });

  return (
    <Carousel activeIndex={activeIndex} next={next} previous={previous}>
      <CarouselIndicators items={props.items} activeIndex={activeIndex} onClickHandler={goToIndex} />
      {slides}
      <CarouselControl direction="prev" directionText="Previous" onClickHandler={previous} />
      <CarouselControl direction="next" directionText="Next" onClickHandler={next} />
    </Carousel>
  );
};

export default TopSellingCard;

import {useEffect, useState} from "react";

const RandomProgramImage: React.FC = () => {
  const [imageSrc, setImageSrc] = useState("");

  useEffect(() => {
    const randomIndex = Math.floor(Math.random() * 4) + 1;
    setImageSrc(`/program-image-${randomIndex}.webp`);
  }, []);

  return <img src={imageSrc} alt="Random Program" className="object-cover" />;
};

export default RandomProgramImage;

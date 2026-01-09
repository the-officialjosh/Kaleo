import {useEffect, useState} from "react";

// Church-focused program images only
const programImages = [
  "/images/program_church_gathering_1767919748536.png",
  "/images/program_youth_event_1767919851751.png",
  "/images/program_community_1767919890028.png",
  "/images/program_workshop_1767919877800.png",
];

const RandomProgramImage: React.FC = () => {
  const [imageSrc, setImageSrc] = useState("");

  useEffect(() => {
    const randomIndex = Math.floor(Math.random() * programImages.length);
    setImageSrc(programImages[randomIndex]);
  }, []);

  return <img src={imageSrc} alt="Program" className="object-cover w-full h-full" />;
};

export default RandomProgramImage;


// Import the necessary libraries
import { library } from '@fortawesome/fontawesome-svg-core'
import {
  faAward,
  faCirclePlus,
  faPencil,
  faPenToSquare,
  faSackXmark,
  faTrashCan,
  faTrophy,
  faUser,
  faPiggyBank,
  faArrowRightFromBracket,
  faBullseye,
  faChevronRight,
  faUserPen,
  faArrowsRotate,
  faLightbulb,
  faArrowRight,
  faArrowLeft,
  faWaveSquare
} from '@fortawesome/free-solid-svg-icons' // Make sure you have the correct icon
import { FontAwesomeIcon } from '@fortawesome/vue-fontawesome'

// Add icons to the library
library.add(
  faPenToSquare,
  faPencil,
  faAward,
  faTrashCan,
  faCirclePlus,
  faSackXmark,
  faUser,
  faTrophy,
  faPiggyBank,
  faArrowRightFromBracket,
  faBullseye,
  faChevronRight,
  faUserPen,
  faArrowsRotate,
  faLightbulb,
  faArrowRight,
  faArrowLeft,
  faWaveSquare
)

// Export the FontAwesomeIcon component
export { FontAwesomeIcon }

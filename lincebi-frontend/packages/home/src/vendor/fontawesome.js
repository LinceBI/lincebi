import Vue from 'vue';

import { config, library } from '@fortawesome/fontawesome-svg-core';
import { FontAwesomeIcon } from '@fortawesome/vue-fontawesome';

import { faClock } from '@fortawesome/free-regular-svg-icons/faClock';
import { faFileLines } from '@fortawesome/free-regular-svg-icons/faFileLines';
import { faSquare } from '@fortawesome/free-regular-svg-icons/faSquare';
import { faSquareCheck } from '@fortawesome/free-regular-svg-icons/faSquareCheck';
import { faWindowMaximize } from '@fortawesome/free-regular-svg-icons/faWindowMaximize';

import { faAngleDown } from '@fortawesome/free-solid-svg-icons/faAngleDown';
import { faAngleUp } from '@fortawesome/free-solid-svg-icons/faAngleUp';
import { faArrowDownShortWide } from '@fortawesome/free-solid-svg-icons/faArrowDownShortWide';
import { faArrowDownWideShort } from '@fortawesome/free-solid-svg-icons/faArrowDownWideShort';
import { faArrowsRotate } from '@fortawesome/free-solid-svg-icons/faArrowsRotate';
import { faAward } from '@fortawesome/free-solid-svg-icons/faAward';
import { faBars } from '@fortawesome/free-solid-svg-icons/faBars';
import { faBook } from '@fortawesome/free-solid-svg-icons/faBook';
import { faBookmark } from '@fortawesome/free-solid-svg-icons/faBookmark';
import { faBoxArchive } from '@fortawesome/free-solid-svg-icons/faBoxArchive';
import { faBriefcase } from '@fortawesome/free-solid-svg-icons/faBriefcase';
import { faBullhorn } from '@fortawesome/free-solid-svg-icons/faBullhorn';
import { faCalendar } from '@fortawesome/free-solid-svg-icons/faCalendar';
import { faCartShopping } from '@fortawesome/free-solid-svg-icons/faCartShopping';
import { faChartLine } from '@fortawesome/free-solid-svg-icons/faChartLine';
import { faChevronLeft } from '@fortawesome/free-solid-svg-icons/faChevronLeft';
import { faChevronRight } from '@fortawesome/free-solid-svg-icons/faChevronRight';
import { faCircleInfo } from '@fortawesome/free-solid-svg-icons/faCircleInfo';
import { faCircleQuestion } from '@fortawesome/free-solid-svg-icons/faCircleQuestion';
import { faCoins } from '@fortawesome/free-solid-svg-icons/faCoins';
import { faCommentsDollar } from '@fortawesome/free-solid-svg-icons/faCommentsDollar';
import { faDatabase } from '@fortawesome/free-solid-svg-icons/faDatabase';
import { faDiagramProject } from '@fortawesome/free-solid-svg-icons/faDiagramProject';
import { faDownload } from '@fortawesome/free-solid-svg-icons/faDownload';
import { faEarthEurope } from '@fortawesome/free-solid-svg-icons/faEarthEurope';
import { faFile } from '@fortawesome/free-solid-svg-icons/faFile';
import { faFileArrowDown } from '@fortawesome/free-solid-svg-icons/faFileArrowDown';
import { faFilter } from '@fortawesome/free-solid-svg-icons/faFilter';
import { faFlask } from '@fortawesome/free-solid-svg-icons/faFlask';
import { faFolder } from '@fortawesome/free-solid-svg-icons/faFolder';
import { faFolderClosed } from '@fortawesome/free-solid-svg-icons/faFolderClosed';
import { faFolderOpen } from '@fortawesome/free-solid-svg-icons/faFolderOpen';
import { faGauge } from '@fortawesome/free-solid-svg-icons/faGauge';
import { faGear } from '@fortawesome/free-solid-svg-icons/faGear';
import { faGraduationCap } from '@fortawesome/free-solid-svg-icons/faGraduationCap';
import { faHandshake } from '@fortawesome/free-solid-svg-icons/faHandshake';
import { faHeart } from '@fortawesome/free-solid-svg-icons/faHeart';
import { faHotel } from '@fortawesome/free-solid-svg-icons/faHotel';
import { faHourglass } from '@fortawesome/free-solid-svg-icons/faHourglass';
import { faHouse } from '@fortawesome/free-solid-svg-icons/faHouse';
import { faIndustry } from '@fortawesome/free-solid-svg-icons/faIndustry';
import { faKey } from '@fortawesome/free-solid-svg-icons/faKey';
import { faLandmark } from '@fortawesome/free-solid-svg-icons/faLandmark';
import { faLayerGroup } from '@fortawesome/free-solid-svg-icons/faLayerGroup';
import { faLightbulb } from '@fortawesome/free-solid-svg-icons/faLightbulb';
import { faList } from '@fortawesome/free-solid-svg-icons/faList';
import { faMagnifyingGlass } from '@fortawesome/free-solid-svg-icons/faMagnifyingGlass';
import { faMapLocation } from '@fortawesome/free-solid-svg-icons/faMapLocation';
import { faPaperclip } from '@fortawesome/free-solid-svg-icons/faPaperclip';
import { faPencil } from '@fortawesome/free-solid-svg-icons/faPencil';
import { faPlus } from '@fortawesome/free-solid-svg-icons/faPlus';
import { faRightFromBracket } from '@fortawesome/free-solid-svg-icons/faRightFromBracket';
import { faRocket } from '@fortawesome/free-solid-svg-icons/faRocket';
import { faSave } from '@fortawesome/free-solid-svg-icons/faSave';
import { faScaleBalanced } from '@fortawesome/free-solid-svg-icons/faScaleBalanced';
import { faScrewdriverWrench } from '@fortawesome/free-solid-svg-icons/faScrewdriverWrench';
import { faShop } from '@fortawesome/free-solid-svg-icons/faShop';
import { faSpinner } from '@fortawesome/free-solid-svg-icons/faSpinner';
import { faStore } from '@fortawesome/free-solid-svg-icons/faStore';
import { faSuitcaseMedical } from '@fortawesome/free-solid-svg-icons/faSuitcaseMedical';
import { faTable } from '@fortawesome/free-solid-svg-icons/faTable';
import { faTruck } from '@fortawesome/free-solid-svg-icons/faTruck';
import { faUpDownLeftRight } from '@fortawesome/free-solid-svg-icons/faUpDownLeftRight';
import { faUpload } from '@fortawesome/free-solid-svg-icons/faUpload';
import { faUser } from '@fortawesome/free-solid-svg-icons/faUser';
import { faUsers } from '@fortawesome/free-solid-svg-icons/faUsers';
import { faWarehouse } from '@fortawesome/free-solid-svg-icons/faWarehouse';
import { faXmark } from '@fortawesome/free-solid-svg-icons/faXmark';

import { faFileAdhoc } from '@lincebi/frontend-common/src/fontawesome/faFileAdhoc';
import { faFileCsv } from '@lincebi/frontend-common/src/fontawesome/faFileCsv';
import { faFileOther } from '@lincebi/frontend-common/src/fontawesome/faFileOther';
import { faFilePdf } from '@lincebi/frontend-common/src/fontawesome/faFilePdf';
import { faFilePrpt } from '@lincebi/frontend-common/src/fontawesome/faFilePrpt';
import { faFileSaiku } from '@lincebi/frontend-common/src/fontawesome/faFileSaiku';
import { faFileSta } from '@lincebi/frontend-common/src/fontawesome/faFileSta';
import { faFileStd } from '@lincebi/frontend-common/src/fontawesome/faFileStd';
import { faFileStolap } from '@lincebi/frontend-common/src/fontawesome/faFileStolap';
import { faFileStp } from '@lincebi/frontend-common/src/fontawesome/faFileStp';
import { faFileUrl } from '@lincebi/frontend-common/src/fontawesome/faFileUrl';
import { faFileWcdf } from '@lincebi/frontend-common/src/fontawesome/faFileWcdf';
import { faFileXjpivot } from '@lincebi/frontend-common/src/fontawesome/faFileXjpivot';
import { faToolCde } from '@lincebi/frontend-common/src/fontawesome/faToolCde';
import { faToolEmbed } from '@lincebi/frontend-common/src/fontawesome/faToolEmbed';
import { faToolJpivot } from '@lincebi/frontend-common/src/fontawesome/faToolJpivot';
import { faToolOther } from '@lincebi/frontend-common/src/fontawesome/faToolOther';
import { faToolPowerBi } from '@lincebi/frontend-common/src/fontawesome/faToolPowerBi';
import { faToolRepositorySynchronizer } from '@lincebi/frontend-common/src/fontawesome/faToolRepositorySynchronizer';
import { faToolSaiku } from '@lincebi/frontend-common/src/fontawesome/faToolSaiku';
import { faToolStadmin } from '@lincebi/frontend-common/src/fontawesome/faToolStadmin';
import { faToolStagile } from '@lincebi/frontend-common/src/fontawesome/faToolStagile';
import { faToolStcard } from '@lincebi/frontend-common/src/fontawesome/faToolStcard';
import { faToolStdashboard } from '@lincebi/frontend-common/src/fontawesome/faToolStdashboard';
import { faToolStolap } from '@lincebi/frontend-common/src/fontawesome/faToolStolap';
import { faToolStpanels } from '@lincebi/frontend-common/src/fontawesome/faToolStpanels';
import { faToolStpivot } from '@lincebi/frontend-common/src/fontawesome/faToolStpivot';
import { faToolStreport } from '@lincebi/frontend-common/src/fontawesome/faToolStreport';
import { faToolStsearch } from '@lincebi/frontend-common/src/fontawesome/faToolStsearch';
import { faToolSuperset } from '@lincebi/frontend-common/src/fontawesome/faToolSuperset';

import '@fortawesome/fontawesome-svg-core/styles.css';

config.autoAddCss = false;

library.add(
	faAngleDown,
	faAngleUp,
	faArrowDownShortWide,
	faArrowDownWideShort,
	faArrowsRotate,
	faAward,
	faBars,
	faBook,
	faBookmark,
	faBoxArchive,
	faBriefcase,
	faBullhorn,
	faCalendar,
	faCartShopping,
	faChartLine,
	faChevronLeft,
	faChevronRight,
	faCircleInfo,
	faCircleQuestion,
	faClock,
	faCoins,
	faCommentsDollar,
	faDatabase,
	faDiagramProject,
	faDownload,
	faEarthEurope,
	faFile,
	faFileAdhoc,
	faFileArrowDown,
	faFileCsv,
	faFileLines,
	faFileOther,
	faFilePdf,
	faFilePrpt,
	faFileSaiku,
	faFileSta,
	faFileStd,
	faFileStolap,
	faFileStp,
	faFileUrl,
	faFileWcdf,
	faFileXjpivot,
	faFilter,
	faFlask,
	faFolder,
	faFolderClosed,
	faFolderOpen,
	faGauge,
	faGear,
	faGraduationCap,
	faHandshake,
	faHeart,
	faHotel,
	faHourglass,
	faHouse,
	faIndustry,
	faKey,
	faLandmark,
	faLayerGroup,
	faLightbulb,
	faList,
	faMagnifyingGlass,
	faMapLocation,
	faPaperclip,
	faPencil,
	faPlus,
	faRightFromBracket,
	faRocket,
	faSave,
	faScaleBalanced,
	faScrewdriverWrench,
	faShop,
	faSpinner,
	faSquare,
	faSquareCheck,
	faStore,
	faSuitcaseMedical,
	faTable,
	faToolCde,
	faToolEmbed,
	faToolJpivot,
	faToolOther,
	faToolPowerBi,
	faToolRepositorySynchronizer,
	faToolSaiku,
	faToolStadmin,
	faToolStagile,
	faToolStcard,
	faToolStdashboard,
	faToolStolap,
	faToolStpanels,
	faToolStpivot,
	faToolStreport,
	faToolStsearch,
	faToolSuperset,
	faTruck,
	faUpDownLeftRight,
	faUpload,
	faUser,
	faUsers,
	faWarehouse,
	faWindowMaximize,
	faXmark,
);

Vue.component('FontAwesomeIcon', FontAwesomeIcon);

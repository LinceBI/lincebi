import Vue from 'vue';

import { config, library } from '@fortawesome/fontawesome-svg-core';
import { FontAwesomeIcon } from '@fortawesome/vue-fontawesome';

import { faClock as farClock } from '@fortawesome/free-regular-svg-icons/faClock';
import { faFileLines as farFileLines } from '@fortawesome/free-regular-svg-icons/faFileLines';
import { faHeart as farHeart } from '@fortawesome/free-regular-svg-icons/faHeart';
import { faSquare as farSquare } from '@fortawesome/free-regular-svg-icons/faSquare';
import { faSquareCheck as farSquareCheck } from '@fortawesome/free-regular-svg-icons/faSquareCheck';
import { faWindowMaximize as farWindowMaximize } from '@fortawesome/free-regular-svg-icons/faWindowMaximize';

import { faAngleDown as fasAngleDown } from '@fortawesome/free-solid-svg-icons/faAngleDown';
import { faAngleUp as fasAngleUp } from '@fortawesome/free-solid-svg-icons/faAngleUp';
import { faArrowDownShortWide as fasArrowDownShortWide } from '@fortawesome/free-solid-svg-icons/faArrowDownShortWide';
import { faArrowDownWideShort as fasArrowDownWideShort } from '@fortawesome/free-solid-svg-icons/faArrowDownWideShort';
import { faArrowsRotate as fasArrowsRotate } from '@fortawesome/free-solid-svg-icons/faArrowsRotate';
import { faAward as fasAward } from '@fortawesome/free-solid-svg-icons/faAward';
import { faBars as fasBars } from '@fortawesome/free-solid-svg-icons/faBars';
import { faBook as fasBook } from '@fortawesome/free-solid-svg-icons/faBook';
import { faBookmark as fasBookmark } from '@fortawesome/free-solid-svg-icons/faBookmark';
import { faBoxArchive as fasBoxArchive } from '@fortawesome/free-solid-svg-icons/faBoxArchive';
import { faBriefcase as fasBriefcase } from '@fortawesome/free-solid-svg-icons/faBriefcase';
import { faBullhorn as fasBullhorn } from '@fortawesome/free-solid-svg-icons/faBullhorn';
import { faCalendar as fasCalendar } from '@fortawesome/free-solid-svg-icons/faCalendar';
import { faCaretDown as fasCaretDown } from '@fortawesome/free-solid-svg-icons/faCaretDown';
import { faCartShopping as fasCartShopping } from '@fortawesome/free-solid-svg-icons/faCartShopping';
import { faChartLine as fasChartLine } from '@fortawesome/free-solid-svg-icons/faChartLine';
import { faChevronLeft as fasChevronLeft } from '@fortawesome/free-solid-svg-icons/faChevronLeft';
import { faChevronRight as fasChevronRight } from '@fortawesome/free-solid-svg-icons/faChevronRight';
import { faCircleInfo as fasCircleInfo } from '@fortawesome/free-solid-svg-icons/faCircleInfo';
import { faCircleQuestion as fasCircleQuestion } from '@fortawesome/free-solid-svg-icons/faCircleQuestion';
import { faCoins as fasCoins } from '@fortawesome/free-solid-svg-icons/faCoins';
import { faCommentsDollar as fasCommentsDollar } from '@fortawesome/free-solid-svg-icons/faCommentsDollar';
import { faDatabase as fasDatabase } from '@fortawesome/free-solid-svg-icons/faDatabase';
import { faDiagramProject as fasDiagramProject } from '@fortawesome/free-solid-svg-icons/faDiagramProject';
import { faDownload as fasDownload } from '@fortawesome/free-solid-svg-icons/faDownload';
import { faEarthEurope as fasEarthEurope } from '@fortawesome/free-solid-svg-icons/faEarthEurope';
import { faFile as fasFile } from '@fortawesome/free-solid-svg-icons/faFile';
import { faFileArrowDown as fasFileArrowDown } from '@fortawesome/free-solid-svg-icons/faFileArrowDown';
import { faFilter as fasFilter } from '@fortawesome/free-solid-svg-icons/faFilter';
import { faFlask as fasFlask } from '@fortawesome/free-solid-svg-icons/faFlask';
import { faFolder as fasFolder } from '@fortawesome/free-solid-svg-icons/faFolder';
import { faFolderClosed as fasFolderClosed } from '@fortawesome/free-solid-svg-icons/faFolderClosed';
import { faFolderOpen as fasFolderOpen } from '@fortawesome/free-solid-svg-icons/faFolderOpen';
import { faGauge as fasGauge } from '@fortawesome/free-solid-svg-icons/faGauge';
import { faGear as fasGear } from '@fortawesome/free-solid-svg-icons/faGear';
import { faGraduationCap as fasGraduationCap } from '@fortawesome/free-solid-svg-icons/faGraduationCap';
import { faHandshake as fasHandshake } from '@fortawesome/free-solid-svg-icons/faHandshake';
import { faHeart as fasHeart } from '@fortawesome/free-solid-svg-icons/faHeart';
import { faHotel as fasHotel } from '@fortawesome/free-solid-svg-icons/faHotel';
import { faHourglass as fasHourglass } from '@fortawesome/free-solid-svg-icons/faHourglass';
import { faHouse as fasHouse } from '@fortawesome/free-solid-svg-icons/faHouse';
import { faIndustry as fasIndustry } from '@fortawesome/free-solid-svg-icons/faIndustry';
import { faKey as fasKey } from '@fortawesome/free-solid-svg-icons/faKey';
import { faLandmark as fasLandmark } from '@fortawesome/free-solid-svg-icons/faLandmark';
import { faLayerGroup as fasLayerGroup } from '@fortawesome/free-solid-svg-icons/faLayerGroup';
import { faLightbulb as fasLightbulb } from '@fortawesome/free-solid-svg-icons/faLightbulb';
import { faList as fasList } from '@fortawesome/free-solid-svg-icons/faList';
import { faMagnifyingGlass as fasMagnifyingGlass } from '@fortawesome/free-solid-svg-icons/faMagnifyingGlass';
import { faMapLocation as fasMapLocation } from '@fortawesome/free-solid-svg-icons/faMapLocation';
import { faPaperclip as fasPaperclip } from '@fortawesome/free-solid-svg-icons/faPaperclip';
import { faPencil as fasPencil } from '@fortawesome/free-solid-svg-icons/faPencil';
import { faPlus as fasPlus } from '@fortawesome/free-solid-svg-icons/faPlus';
import { faRightFromBracket as fasRightFromBracket } from '@fortawesome/free-solid-svg-icons/faRightFromBracket';
import { faRocket as fasRocket } from '@fortawesome/free-solid-svg-icons/faRocket';
import { faSave as fasSave } from '@fortawesome/free-solid-svg-icons/faSave';
import { faScaleBalanced as fasScaleBalanced } from '@fortawesome/free-solid-svg-icons/faScaleBalanced';
import { faScrewdriverWrench as fasScrewdriverWrench } from '@fortawesome/free-solid-svg-icons/faScrewdriverWrench';
import { faShop as fasShop } from '@fortawesome/free-solid-svg-icons/faShop';
import { faSpinner as fasSpinner } from '@fortawesome/free-solid-svg-icons/faSpinner';
import { faStore as fasStore } from '@fortawesome/free-solid-svg-icons/faStore';
import { faSuitcaseMedical as fasSuitcaseMedical } from '@fortawesome/free-solid-svg-icons/faSuitcaseMedical';
import { faTable as fasTable } from '@fortawesome/free-solid-svg-icons/faTable';
import { faTruck as fasTruck } from '@fortawesome/free-solid-svg-icons/faTruck';
import { faUpDownLeftRight as fasUpDownLeftRight } from '@fortawesome/free-solid-svg-icons/faUpDownLeftRight';
import { faUpload as fasUpload } from '@fortawesome/free-solid-svg-icons/faUpload';
import { faUser as fasUser } from '@fortawesome/free-solid-svg-icons/faUser';
import { faUsers as fasUsers } from '@fortawesome/free-solid-svg-icons/faUsers';
import { faWarehouse as fasWarehouse } from '@fortawesome/free-solid-svg-icons/faWarehouse';
import { faXmark as fasXmark } from '@fortawesome/free-solid-svg-icons/faXmark';

import { faFileAdhoc as facFileAdhoc } from '@lincebi/frontend-common/src/fontawesome/faFileAdhoc';
import { faFileCsv as facFileCsv } from '@lincebi/frontend-common/src/fontawesome/faFileCsv';
import { faFileOther as facFileOther } from '@lincebi/frontend-common/src/fontawesome/faFileOther';
import { faFilePdf as facFilePdf } from '@lincebi/frontend-common/src/fontawesome/faFilePdf';
import { faFilePrpt as facFilePrpt } from '@lincebi/frontend-common/src/fontawesome/faFilePrpt';
import { faFileSaiku as facFileSaiku } from '@lincebi/frontend-common/src/fontawesome/faFileSaiku';
import { faFileSta as facFileSta } from '@lincebi/frontend-common/src/fontawesome/faFileSta';
import { faFileStd as facFileStd } from '@lincebi/frontend-common/src/fontawesome/faFileStd';
import { faFileStolap as facFileStolap } from '@lincebi/frontend-common/src/fontawesome/faFileStolap';
import { faFileStp as facFileStp } from '@lincebi/frontend-common/src/fontawesome/faFileStp';
import { faFileUrl as facFileUrl } from '@lincebi/frontend-common/src/fontawesome/faFileUrl';
import { faFileWcdf as facFileWcdf } from '@lincebi/frontend-common/src/fontawesome/faFileWcdf';
import { faFileXjpivot as facFileXjpivot } from '@lincebi/frontend-common/src/fontawesome/faFileXjpivot';
import { faHomeOutline as facHomeOutline } from '@lincebi/frontend-common/src/fontawesome/faHomeOutline';
import { faHomeSolid as facHomeSolid } from '@lincebi/frontend-common/src/fontawesome/faHomeSolid';
import { faToolCde as facToolCde } from '@lincebi/frontend-common/src/fontawesome/faToolCde';
import { faToolEmbed as facToolEmbed } from '@lincebi/frontend-common/src/fontawesome/faToolEmbed';
import { faToolJpivot as facToolJpivot } from '@lincebi/frontend-common/src/fontawesome/faToolJpivot';
import { faToolOther as facToolOther } from '@lincebi/frontend-common/src/fontawesome/faToolOther';
import { faToolPowerBi as facToolPowerBi } from '@lincebi/frontend-common/src/fontawesome/faToolPowerBi';
import { faToolRepositorySynchronizer as facToolRepositorySynchronizer } from '@lincebi/frontend-common/src/fontawesome/faToolRepositorySynchronizer';
import { faToolSaiku as facToolSaiku } from '@lincebi/frontend-common/src/fontawesome/faToolSaiku';
import { faToolStadmin as facToolStadmin } from '@lincebi/frontend-common/src/fontawesome/faToolStadmin';
import { faToolStagile as facToolStagile } from '@lincebi/frontend-common/src/fontawesome/faToolStagile';
import { faToolStcard as facToolStcard } from '@lincebi/frontend-common/src/fontawesome/faToolStcard';
import { faToolStdashboard as facToolStdashboard } from '@lincebi/frontend-common/src/fontawesome/faToolStdashboard';
import { faToolStolap as facToolStolap } from '@lincebi/frontend-common/src/fontawesome/faToolStolap';
import { faToolStpanels as facToolStpanels } from '@lincebi/frontend-common/src/fontawesome/faToolStpanels';
import { faToolStpivot as facToolStpivot } from '@lincebi/frontend-common/src/fontawesome/faToolStpivot';
import { faToolStreport as facToolStreport } from '@lincebi/frontend-common/src/fontawesome/faToolStreport';
import { faToolStsearch as facToolStsearch } from '@lincebi/frontend-common/src/fontawesome/faToolStsearch';
import { faToolSuperset as facToolSuperset } from '@lincebi/frontend-common/src/fontawesome/faToolSuperset';

import '@fortawesome/fontawesome-svg-core/styles.css';

config.autoAddCss = false;

library.add(
	facFileAdhoc,
	facFileCsv,
	facFileOther,
	facFilePdf,
	facFilePrpt,
	facFileSaiku,
	facFileSta,
	facFileStd,
	facFileStolap,
	facFileStp,
	facFileUrl,
	facFileWcdf,
	facFileXjpivot,
	facHomeOutline,
	facHomeSolid,
	facToolCde,
	facToolEmbed,
	facToolJpivot,
	facToolOther,
	facToolPowerBi,
	facToolRepositorySynchronizer,
	facToolSaiku,
	facToolStadmin,
	facToolStagile,
	facToolStcard,
	facToolStdashboard,
	facToolStolap,
	facToolStpanels,
	facToolStpivot,
	facToolStreport,
	facToolStsearch,
	facToolSuperset,
	farClock,
	farFileLines,
	farHeart,
	farSquare,
	farSquareCheck,
	farWindowMaximize,
	fasAngleDown,
	fasAngleUp,
	fasArrowDownShortWide,
	fasArrowDownWideShort,
	fasArrowsRotate,
	fasAward,
	fasBars,
	fasBook,
	fasBookmark,
	fasBoxArchive,
	fasBriefcase,
	fasBullhorn,
	fasCalendar,
	fasCaretDown,
	fasCartShopping,
	fasChartLine,
	fasChevronLeft,
	fasChevronRight,
	fasCircleInfo,
	fasCircleQuestion,
	fasCoins,
	fasCommentsDollar,
	fasDatabase,
	fasDiagramProject,
	fasDownload,
	fasEarthEurope,
	fasFile,
	fasFileArrowDown,
	fasFilter,
	fasFlask,
	fasFolder,
	fasFolderClosed,
	fasFolderOpen,
	fasGauge,
	fasGear,
	fasGraduationCap,
	fasHandshake,
	fasHeart,
	fasHotel,
	fasHourglass,
	fasHouse,
	fasIndustry,
	fasKey,
	fasLandmark,
	fasLayerGroup,
	fasLightbulb,
	fasList,
	fasMagnifyingGlass,
	fasMapLocation,
	fasPaperclip,
	fasPencil,
	fasPlus,
	fasRightFromBracket,
	fasRocket,
	fasSave,
	fasScaleBalanced,
	fasScrewdriverWrench,
	fasShop,
	fasSpinner,
	fasStore,
	fasSuitcaseMedical,
	fasTable,
	fasTruck,
	fasUpDownLeftRight,
	fasUpload,
	fasUser,
	fasUsers,
	fasWarehouse,
	fasXmark,
);

Vue.component('FontAwesomeIcon', FontAwesomeIcon);

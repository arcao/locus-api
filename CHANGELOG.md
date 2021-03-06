# Changelog
All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](http://keepachangelog.com/en/1.0.0/)
and this project adheres to [Semantic Versioning](http://semver.org/spec/v2.0.0.html).

## [Unreleased]

## [0.3.2] - 2018-11-12
### Fixed
- ID of guidance target

## [0.3.1] - 2018-11-12
### Changed
- 'PackWaypoints' renamed to 'PackPoints'
- 'UpdateContainer' now contains point/track ID of active guide
- updated samples to AndroidX

## [0.3.0] - 2018-10-05
### Changed
- 'Storable' class variables have to be initialized in constructor, not in removed 'reset' call
- removed other constructors for 'Storable' class. Use Storable.read if creating instance from existing data
- renamed 'Waypoint' class to 'Point'
- other minor refractoring

## [0.2.25] - 2018-08-14
### Changed
- updated loading of last field note log
- add active dashboard and active live tracking ID into update container
- changes in gradle build system versions
### Fixed
- problem with missing locus-api dependency in locus-api-android module
- geotour attribute for geocaches

## [0.2.22] - 2018-05-30
### Added
- support for Geocaching trackables over API
- `GeoDataExtra.PAR_POI_ALERT_INCLUDE` flag to manually include/exluce certain point from POI Alert

## [0.2.21] - 2018-04-05
### Changed
- [fixed #9](https://github.com/asamm/locus-api/issues/9) - getting info about available Locus versions 

## [0.2.20] - 2018-03-04
### Changed
- updated versions of relevant libraries
### Deprecated
- TracksStats > cumulative elevation parameters

## [pre 0.2.20]
### Long history

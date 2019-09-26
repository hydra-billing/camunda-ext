v1.3 [unreleased]
-------------------
### Breaking changes
-   [#05e0108](https://github.com/latera/camunda-ext/commit/05e0108) Change hydra_account field to hydra_customer_id in Odoo class
-   [#d4a4a88](https://github.com/latera/camunda-ext/commit/d4a4a88) Improve Region and Address class of hid.Hydra

### Features
-   [#ddcbe81](https://github.com/latera/camunda-ext/commit/ddcbe81) Explicitly set Java 8 target
-   [#844fe16](https://github.com/latera/camunda-ext/commit/844fe16) Pass day, month, year and ISO datetime to Imprint Also add proper Formatter to DateTimeUtil
-   [#d0d978a](https://github.com/latera/camunda-ext/commit/d0d978a) Add forceIsEmpty and forseNotEmpty into StringUtil class
-   [#4de3cf9](https://github.com/latera/camunda-ext/commit/4de3cf9) Add mergeNotNull to MapUtil class
-   [#a279edf](https://github.com/latera/camunda-ext/commit/a279edf) Add upperCase and lowerCase to ListUtil class
-   [#2e0a7e0](https://github.com/latera/camunda-ext/commit/2e0a7e0) Add prefix argument to file methods of Order class
-   [#c5c0a2b](https://github.com/latera/camunda-ext/commit/c5c0a2b) Add Message class to hid.Hydra Now you can fetch messages with certain locale (configurable)
-   [#4dfc532](https://github.com/latera/camunda-ext/commit/4dfc532) Improve SQL generator Separate SQL generator (prepareTableQuery) and executor (getTableData)
-   [#9bff882](https://github.com/latera/camunda-ext/commit/9bff882) Get provider, recipient, etc data for document
-   [#07bffbd](https://github.com/latera/camunda-ext/commit/07bffbd) Add getOpfCode method to Ref class
-   [#71e1951](https://github.com/latera/camunda-ext/commit/71e1951) Add named args support to getAvailableServices method
-   [#62be95a](https://github.com/latera/camunda-ext/commit/62be95a) Add README file

### Bugfixes
-   [#14a989a](https://github.com/latera/camunda-ext/commit/14a989a) Fix queryFirstMap and queryFirstList in hid class
-   [#7792d88](https://github.com/latera/camunda-ext/commit/7792d88) Fix add*AddParam return value
-   [#6acfcfc](https://github.com/latera/camunda-ext/commit/6acfcfc) Fix closeObjAddress method arg names
-   [#14d8afd](https://github.com/latera/camunda-ext/commit/14d8afd) Fix mergeParams method

v1.2 [2019-08-25]
---------------------

### Breaking changes
-   [#98c2610](https://github.com/latera/camunda-ext/commit/98c2610) Use only subjectId for getSubject at hid.Hydra
-   [#f84d1f7](https://github.com/latera/camunda-ext/commit/f84d1f7) Use only docId in getDocument at hid.Hydra
-   [#cdea4af](https://github.com/latera/camunda-ext/commit/cdea4af) Rename get\*Addresses to get\*AddressesBy in hid.Hydra class
-   [#9479085](https://github.com/latera/camunda-ext/commit/9479085) Sync equipment and subject add param methods in hid.Hydra
-   [#3e8c3cb](https://github.com/latera/camunda-ext/commit/3e8c3cb) Use environment variables to store integrations credentials
-   [#bd61a2c](https://github.com/latera/camunda-ext/commit/bd61a2c) Move subject add param value type detect to function at hid.Hydra
-   [#b77eb43](https://github.com/latera/camunda-ext/commit/b77eb43) Remove good tags field from hid.Hydra
-   [#6e5fe86](https://github.com/latera/camunda-ext/commit/6e5fe86) Use HOMS_TOKEN instead of HOMS_PASSWORD in HOMS class
-   [#487cdb7](https://github.com/latera/camunda-ext/commit/487cdb7) Use SIMPLE_DATE_FORMAT for Date class format Instead of SIMPLE_DATE_TIME_FORMAT
-   [#50497cd](https://github.com/latera/camunda-ext/commit/50497cd) Limit SELECT results for getEnityBy methods in hid.Hydra

### Features
-   [#7b65aa1](https://github.com/latera/camunda-ext/commit/7b65aa1) Add snakeCase and Map keys handlers to StringUtil class
-   [#f974d24](https://github.com/latera/camunda-ext/commit/f974d24) Add Odoo connector class
-   [#e5493e9](https://github.com/latera/camunda-ext/commit/e5493e9) Add support of v1 to Hoper class
-   [#1f40b31](https://github.com/latera/camunda-ext/commit/1f40b31) Add Person methods to Hoper class
-   [#f0fab2d](https://github.com/latera/camunda-ext/commit/f0fab2d) Allow to pass null fields value to Hoper and Odoo methods
-   [#def0331](https://github.com/latera/camunda-ext/commit/def0331) Add getEntities method to hoper.Entity class
-   [#d4233f8](https://github.com/latera/camunda-ext/commit/d4233f8) Support pagination in Hoper.Entity class
-   [#70804a5](https://github.com/latera/camunda-ext/commit/70804a5) Add getPersons method to hoper.Hydra class
-   [#2fcc6a0](https://github.com/latera/camunda-ext/commit/2fcc6a0) Add company methods to hoper.Hydra class
-   [#f0bccc0](https://github.com/latera/camunda-ext/commit/f0bccc0) Add addresses methods to hoper.Hydra class
-   [#918be40](https://github.com/latera/camunda-ext/commit/918be40) Add customer methods to hoper.Hydra class
-   [#396ec2c](https://github.com/latera/camunda-ext/commit/396ec2c) Add account methods to hoper.Hydra class
-   [#25506bf](https://github.com/latera/camunda-ext/commit/25506bf) Add contract methods to hoper.Hydra class
-   [#09867b8](https://github.com/latera/camunda-ext/commit/09867b8) Add equipment methods to hoper.Hydra class
-   [#9e96219](https://github.com/latera/camunda-ext/commit/9e96219) Add subscription methods to hoper.Hydra class
-   [#d6f9f9c](https://github.com/latera/camunda-ext/commit/d6f9f9c) Add overdraft methods to hid.Hydra class
-   [#e8f0554](https://github.com/latera/camunda-ext/commit/e8f0554) Add refreshContractTree method to hid.Hydra class
-   [#9dfbfff](https://github.com/latera/camunda-ext/commit/9dfbfff) Add processCustomer method to hid.Hydra
-   [#893c659](https://github.com/latera/camunda-ext/commit/893c659) Add aliases for methods to hid.Hydra
-   [#955047b](https://github.com/latera/camunda-ext/commit/955047b) Add iso method to DateTimeUtils class
-   [#a5ce97b](https://github.com/latera/camunda-ext/commit/a5ce97b) Set current firmId in hoper.Hydra methods
-   [#b16f515](https://github.com/latera/camunda-ext/commit/b16f515) Add some wrapped put methods into hoper.Hydra
-   [#3d6eb4c](https://github.com/latera/camunda-ext/commit/3d6eb4c) Allow to use multiple add params for subject at hid.Hydra
-   [#5bfc89a](https://github.com/latera/camunda-ext/commit/5bfc89a) Add deleteSubjectAddParam into hid.Hydra
-   [#702ad63](https://github.com/latera/camunda-ext/commit/702ad63) Add good additional params methods into hid.Hydra
-   [#115366f](https://github.com/latera/camunda-ext/commit/115366f) Add document add param methods into hid.Hydra
-   [#69293ae](https://github.com/latera/camunda-ext/commit/69293ae) Add get refs methods into hid.Hydra
-   [#79dfc74](https://github.com/latera/camunda-ext/commit/79dfc74) Add getAccountsBy method into hid.Hydra
-   [#ea8f197](https://github.com/latera/camunda-ext/commit/ea8f197) Allow to use named args for account metods in hid.Hydra
-   [#05c0550](https://github.com/latera/camunda-ext/commit/05c0550) Allow to pass GStringImpl to getTable methods in hid.Hydra
-   [#4da8100](https://github.com/latera/camunda-ext/commit/4da8100) Add contract app and add agreement methods to hid.Hydra
-   [#1232857](https://github.com/latera/camunda-ext/commit/1232857) Allow passing GStringImpl to hid.execute
-   [#aeed9d1](https://github.com/latera/camunda-ext/commit/aeed9d1) Allow to pass named args to update methods in Odoo
-   [#05fd797](https://github.com/latera/camunda-ext/commit/05fd797) Add null escaping to Planado class methods
-   [#bbc4212](https://github.com/latera/camunda-ext/commit/bbc4212) Add serv scheme methods to hid.Hydra
-   [#3f1cc56](https://github.com/latera/camunda-ext/commit/3f1cc56) Add OTRS connector For OTRS v6
-   [#6901d2f](https://github.com/latera/camunda-ext/commit/6901d2f) Allow to pass object id to net services methods in hid.Hydra
-   [#de6af59](https://github.com/latera/camunda-ext/commit/de6af59) Add generateRandomString to StringUtil class
-   [#7238f55](https://github.com/latera/camunda-ext/commit/7238f55) Add priorityId to otrs.Ticket class
-   [#379362a](https://github.com/latera/camunda-ext/commit/379362a) Allow to not pass data to print form in Imprint.print Pass order data by default
-   [#81e1070](https://github.com/latera/camunda-ext/commit/81e1070) Add stream cast functions to IO class
-   [#a48612a](https://github.com/latera/camunda-ext/commit/a48612a) Add Minio connector class
-   [#d17b229](https://github.com/latera/camunda-ext/commit/d17b229) Allow to use Groovy native named args in HOMS.createOrder
-   [#628ba77](https://github.com/latera/camunda-ext/commit/628ba77) Add methods for receiving actual charge logs and account periodic sums into hid.Hydra
-   [#80d116b](https://github.com/latera/camunda-ext/commit/80d116b) Add methods for managing subject comments into hid.Hydra
-   [#f9bca96](https://github.com/latera/camunda-ext/commit/f9bca96) Add isInteger, isFloat, isNumber to Numeric class
-   [#719c86a](https://github.com/latera/camunda-ext/commit/719c86a) Add MapUtil and ListUtil classes with useful methods Like parse, nvl, isList, isMap
-   [#29bb45d](https://github.com/latera/camunda-ext/commit/29bb45d) Add CSV util class with useful methods
-   [#7ea912c](https://github.com/latera/camunda-ext/commit/7ea912c) Add useful static and non-static methods to Order class
-   [#a93ddf4](https://github.com/latera/camunda-ext/commit/a93ddf4) Allow to pass list as 'in' or 'not in' values in hid.Hydra
-   [#032016d](https://github.com/latera/camunda-ext/commit/032016d) Add support of LocalDate into DateTimeUtil
-   [#2e5bf3c](https://github.com/latera/camunda-ext/commit/2e5bf3c) Add doc subject methods into hid.Hydra Also allow to fetch documents by member and manager roles
-   [#a5837ea](https://github.com/latera/camunda-ext/commit/a5837ea) Add putDocument method into hid.Hydra
-   [#c2ae664](https://github.com/latera/camunda-ext/commit/c2ae664) Add invoice content get methods into hid.Hydra
-   [#c456ab8](https://github.com/latera/camunda-ext/commit/c456ab8) Add bill documents and content methods into hid.Hydra
-   [#1c7fd3f](https://github.com/latera/camunda-ext/commit/1c7fd3f) Move hoper file methods into separate class
-   [#e1d6042](https://github.com/latera/camunda-ext/commit/e1d6042) Allow to create order in HOMS with no data
-   [#7fed977](https://github.com/latera/camunda-ext/commit/7fed977) Use functions for cache in ref and getTableColumns in hid class
-   [#61034f0](https://github.com/latera/camunda-ext/commit/61034f0) Improve DateTimeUtil methods
-   [#ce8d885](https://github.com/latera/camunda-ext/commit/ce8d885) Return password from addCustomerNetServiceAccess and addCustomerAppAccess in hid.Hydra
-   [#c347496](https://github.com/latera/camunda-ext/commit/c347496) Allow to use native named args for add*AddParam functions in hid.Hydra
-   [#0236303](https://github.com/latera/camunda-ext/commit/0236303) Add functions for managing doc binds into hid.Hydra
-   [#3484eb7](https://github.com/latera/camunda-ext/commit/3484eb7) Transparently convert date types to ISO string and backwards in Order class
-   [#1be3bef](https://github.com/latera/camunda-ext/commit/1be3bef) Improve type checking
-   [#432357d](https://github.com/latera/camunda-ext/commit/432357d) Add joinNonEmpty method to StringUtil
-   [#b8b21d9](https://github.com/latera/camunda-ext/commit/b8b21d9) Add deepCamelizeKeys and deepSnakeCaseKeys methods into MapUtil class
-   [#6d6d1b8](https://github.com/latera/camunda-ext/commit/6d6d1b8) Add Dadata connectors
-   [#6df2424](https://github.com/latera/camunda-ext/commit/6df2424) Add GoogleMaps connector
-   [#b067bca](https://github.com/latera/camunda-ext/commit/b067bca) Move connectors to their dirs
-   [#fac820f](https://github.com/latera/camunda-ext/commit/fac820f) Pass resellerId to Hydra REST API
-   [#fb24b85](https://github.com/latera/camunda-ext/commit/fb24b85) Add method for updating quick search into hid.Hydra

### Bugfixes
-   [#0dbb535](https://github.com/latera/camunda-ext/commit/0dbb535) Fix runCommand quotes usage in Console class
-   [#06518c1](https://github.com/latera/camunda-ext/commit/06518c1) Change Planado methods return values
-   [#79c55e0](https://github.com/latera/camunda-ext/commit/79c55e0) Use format string with TZ only for ZonedDateTime
-   [#35907c0](https://github.com/latera/camunda-ext/commit/35907c0) Fix overdraft methods missing default param
-   [#044b388](https://github.com/latera/camunda-ext/commit/044b388) Fix get refs method in hid.Hydra
-   [#4939dc1](https://github.com/latera/camunda-ext/commit/4939dc1) Fix get document and add params methods in hid.Hydra
-   [#7252d7c](https://github.com/latera/camunda-ext/commit/7252d7c) Use correct workflow for add agreement and contract in hid.Hydra
-   [#ebf6238](https://github.com/latera/camunda-ext/commit/ebf6238) Fix contract app adn add agreement creation in hid.Hydra
-   [#1852a7e](https://github.com/latera/camunda-ext/commit/1852a7e) Fix SMTP port cast to Integer in Mail class
-   [#4b055f5](https://github.com/latera/camunda-ext/commit/4b055f5) Explicit convert byte[] to String in http logging
-   [#e616064](https://github.com/latera/camunda-ext/commit/e616064) Fix Planado createUser/Company return value
-   [#0796d8d](https://github.com/latera/camunda-ext/commit/0796d8d) Fix good add param types in hid.Hydra
-   [#8181071](https://github.com/latera/camunda-ext/commit/8181071) Add workaround to [HttpBuilderNG issue|https://github.com/http-builder-ng/http-builder-ng/issues/210]
-   [#a0f11dd](https://github.com/latera/camunda-ext/commit/a0f11dd) Fix get contract app/add agreement in hid.Hydra
-   [#badecdc](https://github.com/latera/camunda-ext/commit/badecdc) Fix refreshContractsTree method in Hydra v5
-   [#88f0ec5](https://github.com/latera/camunda-ext/commit/88f0ec5) Do not log files content in Imprint.print method
-   [#f51e713](https://github.com/latera/camunda-ext/commit/f51e713) Do not log files content in HOMS.attach_files method
-   [#293efaa](https://github.com/latera/camunda-ext/commit/293efaa) Fix wrong type cast of getSubjectParamType method in hid.Hydra
-   [#8e6f18f](https://github.com/latera/camunda-ext/commit/8e6f18f) Fix fetching free phone numbers by tel code in hid.Hydra
-   [#16c5551](https://github.com/latera/camunda-ext/commit/16c5551) Fix list of lists JSON escaping
-   [#42b0665](https://github.com/latera/camunda-ext/commit/42b0665) Fix STARTTLS issue with GMail SMTP
-   [#5db94c4](https://github.com/latera/camunda-ext/commit/5db94c4) Change contact filling in Planado class

v1.1 [2019-05-05]
---------------------
### Features
-   [#9a78333](https://github.com/latera/camunda-ext/commit/9a78333) Added SimpleLogger
-   [#08e46b2](https://github.com/latera/camunda-ext/commit/08e46b2) Add utils class for Oracle, Order, IO, JSON, String and DateUtil
-   [#2301d54](https://github.com/latera/camunda-ext/commit/2301d54) Add user and password auth to HTTPRestProcessor class
-   [#e2634a4](https://github.com/latera/camunda-ext/commit/e2634a4) Add class for HOMS API
-   [#94cc1cb](https://github.com/latera/camunda-ext/commit/94cc1cb) Add class for Imprint API
-   [#b2030d9](https://github.com/latera/camunda-ext/commit/b2030d9) Add class for HID API
-   [#d51242f](https://github.com/latera/camunda-ext/commit/d51242f) Add class for Hydra (via HID) API
-   [#1940b23](https://github.com/latera/camunda-ext/commit/1940b23) Move build from Ant to Maven
-   [#8f580d2](https://github.com/latera/camunda-ext/commit/8f580d2) Added Mail and Planado connectors
-   [#f904daf](https://github.com/latera/camunda-ext/commit/f904daf) Added options to hide request & response body for RESTProcessor
-   [#97ed434](https://github.com/latera/camunda-ext/commit/97ed434) Added utils: Numeric, Order
-   [#8e37bf1](https://github.com/latera/camunda-ext/commit/8e37bf1) Some logging methods usage fixes

v1.0 [2018-10-22]
---------------------
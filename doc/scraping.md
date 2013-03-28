# Rate My Professors

#### getting a list of all the U.S. schools
for each state_abbrev:
  http://www.ratemyprofessors.com/SelectSchool.jsp?country=0&stateselect=UT
  
  
#### getting a list of all the teachers

for each page, sid:
  http://www.ratemyprofessors.com/SelectTeacher.jsp?sid=2765&pageNo=2
  
  
#### getting teacher ratings

for each page, teacher:
  http://www.ratemyprofessors.com/ShowRatings.jsp?tid=1263877&pageNo=2

#### getting school ratings

for each school:
  http://www.ratemyprofessors.com/campusRatings.jsp?sid=2765&pageNo=2

## RSS

every search page looks to have an RSS feed, this could be used to
actively keep an up to date database of incoming ratings. It could
also be a better way of scraping since it's easier to parse than html.

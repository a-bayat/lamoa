# Lamoa
This is a simple scala app which tries to find appropriate subtitle for given movie directory ;) 
##TODO LIST
- [ ] Add **Levenshtein distance** algorithm and write down test
- [ ] Add Configuration file to config project
   - Specify which site look into for subtitle
   - Define patterns for Leven algorithm
   - Specify Which language to get
- ##### Scraping Process:
    - [ ] extract file name
    - [ ] apply given *(from config file)* patterns on the name
    - [ ] check which pattern is most similar to file name
        - display user is the most similar matched name, if user approved download, rename and put next to main file
        unless user enter the file name 
      

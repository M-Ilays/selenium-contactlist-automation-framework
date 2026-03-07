import openpyxl

# Create workbook and sheets
wb = openpyxl.Workbook()
ws = wb.active
ws.title = 'Login'  # Changed from 'Sheet1' to 'Login'

# Login Sheet - Header
ws['A1'] = 'Email'
ws['B1'] = 'Password'
ws['C1'] = 'Expected Result'

# Login Sheet - Test Data
ws['A2'] = 'm46328690@gmail.com'
ws['B2'] = '1234567'
ws['C2'] = 'Pass'

ws['A3'] = 'm46328690@gmail.com'
ws['B3'] = 'wrongpassword'
ws['C3'] = 'Fail'

ws['A4'] = 'invalid@test.com'
ws['B4'] = '12345'
ws['C4'] = 'Fail'

# SignUp Sheet - Create and setup
ws2 = wb.create_sheet('SignUp')
ws2['A1'] = 'First Name'
ws2['B1'] = 'Last Name'
ws2['C1'] = 'Email'
ws2['D1'] = 'Password'
ws2['E1'] = 'Expected Result'

ws2['A2'] = 'Muhammad'
ws2['B2'] = 'Ilyas'
ws2['C2'] = 'newuser54321@test.com'
ws2['D2'] = '1234567'
ws2['E2'] = 'Pass'

ws2['A3'] = 'Test'
ws2['B3'] = 'User'
ws2['C3'] = 'm46328690@gmail.com'
ws2['D3'] = '1234567'
ws2['E3'] = 'Fail'

# Save to both locations
wb.save('src/test/resources/testdata/Data.xlsx')
wb.save('src/main/resources/testdata/Data.xlsx')

print('Excel file created successfully!')
print(f'Login Row 2 (Row 2): Expected Result = {ws["C2"].value}')
print(f'Login Row 3 (Row 3): Expected Result = {ws["C3"].value}')
print(f'Login Row 4 (Row 4): Expected Result = {ws["C4"].value}')
print(f'SignUp Row 2 (Row 2): Expected Result = {ws2["E2"].value}')
print(f'SignUp Row 3 (Row 3): Expected Result = {ws2["E3"].value}')

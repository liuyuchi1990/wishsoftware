Const resultRow As Integer = 18
Dim MyArray(10, 2)

Sub Copy_Table_To_Word()
    On Error Resume Next
    Application.AskToUpdateLinks = False
    Application.AskToUpdateLinks = False     '关闭程序询问更新链接提示
    Application.DisplayAlerts = False
    ThisWorkbook.UpdateLinks = xlUpdateLinksAlways  '更新链接
    Dim wdApp As Word.Application
    Dim wdDoc As Word.Document
    Dim hasChangedValue As Boolean
    Dim usedRow, headLevel, formatIdentity, mergeCellsCount, language, oldLanguage As Integer
    Dim docFile, sheetName, controlCheck, orient, sourceText As String
    docFile = Sheets("Main").Cells(9, 6)
    Set wdApp = New Word.Application
    Set wdDoc = wdApp.Documents.Open(docFile)

    If wdDoc Is Nothing Then
        MsgBox "Please select a target word file!", vbOKOnly + vbInformation, "Error!"
        GoTo EndSub
    End If

    Dim formatSettings, oldFormatSettings As Variant
    With Sheets("Main")
        usedRow = .[F65536].End(xlUp).Row
        If usedRow >= resultRow Then
            .Range("F" & resultRow & ":J" & usedRow).ClearContents
            .Range("F" & resultRow & ":J" & usedRow).Interior.Pattern = xlNone
        End If
        usedRow = .[A65536].End(xlUp).Row
        formatSettings = .Range("A" & resultRow & ":E" & usedRow).Value
        usedRow = .[AA65536].End(xlUp).Row
        oldFormatSettings = .Range("AA2" & ":AE" & usedRow).Value
        language = .Cells(10, 5)
        oldLanguage = .Cells(1, 27)
    End With

    For i = 1 To wdDoc.Tables.count
        sheetName = wdDoc.Tables(i).Title
        If sheetName <> "" Then
            Sheets(sheetName).Select
            If Err = 0 Then
                headLevel = 0
                formatIdentity = 0
                orient = "P"
                For l = 1 To UBound(formatSettings)
                    If formatSettings(l, 1) = sheetName Then
                        headLevel = formatSettings(l, 3)
                        formatIdentity = formatSettings(l, 4)
                        orient = UCase(formatSettings(l, 5))
                        Exit For
                    End If
                Next l
                controlCheck = Prepare_Source_Data(sheetName, formatIdentity)

                hasChangedValue = False
                If oldLanguage <> language Then
                    hasChangedValue = True
                ElseIf wdDoc.Tables(i).Cell(1, 1).Tables().count = 0 Then
                    hasChangedValue = True
                ElseIf wdDoc.Tables(i).Cell(1, 1).Tables(1).Rows.count <> Sheets("Temp").UsedRange.Rows.count Or _
                       wdDoc.Tables(i).Cell(1, 1).Tables(1).Columns.count <> Sheets("Temp").UsedRange.Columns.count Then
                    hasChangedValue = True
                Else
                    For o = 1 To UBound(oldFormatSettings)
                        If oldFormatSettings(o, 1) = sheetName Then
                            If headLevel <> oldFormatSettings(o, 3) Or formatIdentity <> oldFormatSettings(o, 4) Or orient <> UCase(oldFormatSettings(o, 5)) Then
                                hasChangedValue = True
                            End If
                            Exit For
                        End If
                    Next o
                End If

                If hasChangedValue = True Then
                    Format_Copied_Table wdDoc, i, headLevel, formatIdentity, orient
                End If
                If wdDoc.Tables(i).Cell(1, 1).Tables(1).Rows(1).Cells.count = Sheets("Temp").UsedRange.Columns.count Then
                    Sheets("Temp").UsedRange.Rows(1).UnMerge
                End If
                With wdDoc.Tables(i).Cell(1, 1).Tables(1)
                    For r = 1 To .Rows.count
                        mergeCellsCount = 0
                        For c = 1 To .Rows(r).Cells.count
                            sourceText = Sheets("Temp").UsedRange.Rows(r).Cells(c + mergeCellsCount).Text
                            sourceText = Replace(sourceText, Chr(10), Chr(13))
                            sourceText = sourceText & Chr(13) & Chr(7)
                            If .Rows(r).Cells(c).Range.Text <> sourceText Then
                                .Rows(r).Cells(c).Range.Text = Sheets("Temp").UsedRange.Rows(r).Cells(c + mergeCellsCount).Text
                                Select Case Sheets("Temp").Cells(r, c + mergeCellsCount).HorizontalAlignment
                                    Case xlRight
                                        .Rows(r).Cells(c).Range.ParagraphFormat.Alignment = wdAlignParagraphRight
                                    Case xlLeft
                                        .Rows(r).Cells(c).Range.ParagraphFormat.Alignment = wdAlignParagraphLeft
                                    Case xlCenter
                                        .Rows(r).Cells(c).Range.ParagraphFormat.Alignment = wdAlignParagraphCenter
                                End Select
                                hasChangedValue = True
                            End If
                            If Sheets("Temp").UsedRange.Rows(r).Cells(c + mergeCellsCount).mergeCells Then
                                mergeCellsCount = mergeCellsCount + Sheets("Temp").UsedRange.Rows(r).Cells(c + mergeCellsCount).MergeArea.Columns.count - 1
                            End If
                        Next c
                    Next r
                End With

                Sheets("Main").Cells(resultRow + i - 1, 7) = sheetName 'Record mapped sheet in column F
                If hasChangedValue = True Then
                    Sheets("Main").Cells(resultRow + i - 1, 8) = "Yes" 'Mark the copy result
                    Sheets("Main").Cells(resultRow + i - 1, 9) = "Yes" 'Mark the update result
                    Sheets("Main").Cells(resultRow + i - 1, 10) = controlCheck 'Mark the control result
                End If
            End If
            Sheets("Main").Cells(resultRow + i - 1, 6) = sheetName 'Record tables in column E
            Err.Clear
        End If
          For j = 0 To UBound(MyArray)

        If MyArray(j, 0) <> "" Then
        x = (MyArray(j, 2) - MyArray(j, 1))
        y = (MyArray(j, 2) - MyArray(j, 1))
        Application.Wait (Now + TimeValue("0:00:02"))
          wdDoc.Range(wdDoc.Tables(i).Cell(1, 1).Tables(1).Cell(MyArray(j, 0), MyArray(j, 1)).Range.Start, wdDoc.Tables(i).Cell(1, 1).Tables(1).Cell(MyArray(j, 0), MyArray(j, 2)).Range.End).Cells.Merge 'hebing

        For k = 0 To UBound(MyArray)
        If MyArray(k, 0) = MyArray(j, 0) Then
        MyArray(k, 1) = MyArray(k, 1) - x
        MyArray(k, 2) = MyArray(k, 2) - y
        End If
        Next



      End If
       Next

        wdDoc.save
    Next i

    Set_After_Copy
    GetBlankPage wdDoc
    wdDoc.save
    wdDoc.Close
    Set wdDoc = Nothing
EndSub:
    wdApp.Quit
    Set wdApp = Nothing
End Sub

Function Prepare_Source_Data(ByVal sheetName As String, ByVal formatIdentity As Integer) As String
Application.AskToUpdateLinks = False
    On Error Resume Next
    Dim maxColumn, maxRow, usedColumn, usedRow, hasRemark As Integer
    Sheets("Temp").Cells.Delete

    With Sheets(sheetName)
        .Select
        For usedRow = .[A65536].End(xlUp).Row To 1 Step -1
            If LCase(.Cells(usedRow, 1)) = "end" Then
                Exit For
            End If
        Next usedRow

        For usedColumn = .[IV1].End(xlToLeft).Column To 1 Step -1
            If LCase(.Cells(1, usedColumn)) = "end" Then
                Exit For
            End If
        Next usedColumn

        'copy used area only
        If usedRow > 1 And usedColumn > 1 Then
            .Range(.Cells(1, 1), .Cells(usedRow - 1, usedColumn - 1)).Copy
            maxRow = usedRow - 1
            maxColumn = usedColumn - 1
        Else
            .UsedRange.Copy
            maxRow = .UsedRange.Rows.count
            maxColumn = .UsedRange.Columns.count
        End If
    End With

    With Sheets("Temp")
        .Select
        .Range(.Cells(1, 1), .Cells(maxRow, maxColumn)).Select
        .Paste
        .Range(.Cells(1, 1), .Cells(maxRow, maxColumn)).WrapText = False
        .Buttons("Line1").Delete
        .Buttons("Line2").Delete
        .Buttons("Line3").Delete
        .Buttons("Line4").Delete
         Dim ra As Range
        Erase MyArray
   Num = 0
        For Each ra In Selection 'chaifen
 If ra.mergeCells Then
 ra.Select
  MyArray(Num, 0) = ra.Row
 MyArray(Num, 1) = ra.Column
 MyArray(Num, 2) = ra.Column + ra.MergeArea.Columns.count - 1
 Num = Num + 1
 ra.UnMerge
 End If
 Next ra

        For c = 1 To maxColumn
            .Columns(c).ColumnWidth = 100
            DoEvents
            .Columns(c).EntireColumn.AutoFit
        Next c

        For i = maxRow To 2 Step -1
            .Rows(i).rowHeight = Sheets(sheetName).Rows(i).rowHeight
            If Trim(LCase(.Cells(i, 1))) = "control" Then
                If controlCheck = "" Then
                    controlCheck = "OK"
                End If
                If controlCheck = "OK" Then
                    For j = 1 To maxColumn
                        If LCase(.Cells(i, j)) = "not ok" Then
                            controlCheck = "Not OK"
                            Exit For
                        End If
                    Next j
                End If
                .Rows(i).Select
                Selection.Delete Shift:=xlUp
            End If
        Next i

        For j = maxColumn To 2 Step -1
            If Trim(LCase(.Cells(1, j))) = "control" Then
                If controlCheck = "" Then
                    controlCheck = "OK"
                End If
                If controlCheck = "OK" Then
                    For i = 1 To maxRow
                        If LCase(.Cells(i, j)) = "not ok" Then
                            controlCheck = "Not OK"
                            Exit For
                        End If
                    Next i
                End If
                .Columns(j).Select
                Selection.Delete Shift:=xlToLeft
            End If
        Next j

        If Trim(LCase(.Cells(1, 1))) = "control" Then
            If controlCheck = "" Then
                controlCheck = "OK"
            End If
            If controlCheck = "OK" Then
                For i = 1 To maxRow
                    If LCase(.Cells(i, 1)) = "not ok" Then
                        controlCheck = "Not OK"
                        Exit For
                    End If
                Next i
                For j = 1 To maxColumn
                    If LCase(.Cells(1, j)) = "not ok" Then
                        controlCheck = "Not OK"
                        Exit For
                    End If
                Next j
            End If
            .Rows(1).Select
            Selection.Delete Shift:=xlUp
            .Columns(1).Select
            Selection.Delete Shift:=xlToLeft
        End If

        hasRemark = .UsedRange.Columns.count Mod 2
        .UsedRange.HorizontalAlignment = xlRight
        .UsedRange.VerticalAlignment = xlBottom
        .Columns(1).HorizontalAlignment = xlLeft
        .Columns(1).VerticalAlignment = xlBottom
        If hasRemark And formatIdentity <> 10 Then
            .Columns(2).HorizontalAlignment = xlCenter
            .Columns(2).VerticalAlignment = xlBottom
        End If
        If .Cells(1, 3).mergeCells = True Then
            .Rows(1).HorizontalAlignment = xlCenter
        End If
    End With

    Prepare_Source_Data = controlCheck
End Function

Sub Format_Copied_Table(ByRef wdDoc As Word.Document, ByVal tableIndex As Integer, ByVal headLevel As Integer, ByVal formatIdentity As Integer, ByVal orient As String)
    On Error Resume Next
    Application.AskToUpdateLinks = False
    If wdDoc.Tables(tableIndex).Cell(1, 1) Is Nothing Then
        Exit Sub
    End If

    Dim mergedTable As Boolean
    Dim hasRemark, mergedCells1, mergedCells2, colCount, language As Integer
    Dim firstWidth, remarkWidth, dataWidth, dataWidth2, spaceWidth, rowHeight, fontSize As Double

    language = Sheets("Main").Cells(10, 5)
    With Sheets("Temp")
        colCount = .UsedRange.Columns.count
        mergedTable = .Cells(1, 3).mergeCells
        If mergedTable Then
            .UsedRange.Rows(1).UnMerge
        End If
        .UsedRange.Copy
    End With

    hasRemark = colCount Mod 2
    If orient = "" Then
        orient = "P"
    End If
    With wdDoc.Tables(tableIndex).Cell(1, 1)
        .LeftPadding = 0
        .RightPadding = 0
        .TopPadding = 0
        .Row.Alignment = wdAlignRowRight

        If orient = "P" Then
            If formatIdentity = 0 Or formatIdentity = 10 Then
                .Width = Application.CentimetersToPoints(16 - headLevel)
            Else
                .Width = Application.CentimetersToPoints(15 - headLevel)
            End If
        Else
            .Width = Application.CentimetersToPoints(24.7 - headLevel)
        End If
        .Select
    End With

    If wdDoc.ActiveWindow.Selection.PageSetup.Orientation = wdOrientPortrait And orient = "L" Then
        wdDoc.ActiveWindow.Selection.InsertBreak Type:=wdSectionBreakNextPage
        wdDoc.Tables(tableIndex).Cell(1, 1).Select
        wdDoc.ActiveWindow.Selection.MoveDown Unit:=wdLine, count:=1
        wdDoc.ActiveWindow.Selection.InsertBreak Type:=wdSectionBreakNextPage
        wdDoc.Tables(tableIndex).Cell(1, 1).Select
        wdDoc.ActiveWindow.Selection.PageSetup.Orientation = wdOrientLandscape
    End If
    If wdDoc.ActiveWindow.Selection.PageSetup.Orientation = wdOrientLandscape And orient = "P" Then
        wdDoc.ActiveWindow.Selection.InsertBreak Type:=wdSectionBreakNextPage
        wdDoc.Tables(tableIndex).Cell(1, 1).Select
        wdDoc.ActiveWindow.Selection.MoveDown Unit:=wdLine, count:=1
        wdDoc.ActiveWindow.Selection.InsertBreak Type:=wdSectionBreakNextPage
        wdDoc.Tables(tableIndex).Cell(1, 1).Select
        wdDoc.ActiveWindow.Selection.PageSetup.Orientation = wdOrientPortrait
    End If

    With wdDoc.ActiveWindow.Selection
        .Delete
        .PageSetup.LeftMargin = Application.CentimetersToPoints(2.5)
        .PageSetup.RightMargin = Application.CentimetersToPoints(2.5)
        .PageSetup.TopMargin = Application.CentimetersToPoints(2.8)
        .PageSetup.BottomMargin = Application.CentimetersToPoints(2.8)
        .PasteExcelTable False, False, False
        .Comments.Add .Range, wdDoc.Tables(tableIndex).Title
        .Range.Paragraphs.SpaceBefore = 0
        .Range.Paragraphs.SpaceAfter = 0
        .Range.Paragraphs.LineSpacingRule = wdLineSpaceSingle
    End With

    With wdDoc.Tables(tableIndex).Cell(1, 1).Tables(1)
        .AllowAutoFit = True
        .AutoFitBehavior wdAutoFitFixed
        Select Case colCount
            Case 3
                fontSize = 11
                firstWidth = Application.CentimetersToPoints(11.25)
                remarkWidth = Application.CentimetersToPoints(1.5)
                dataWidth = Application.CentimetersToPoints(3.25)
            Case 4, 5 '2 data col, size 11, 11
                fontSize = 11
                Select Case formatIdentity
                    Case 1
                        firstWidth = Application.CentimetersToPoints(8.25)
                        remarkWidth = Application.CentimetersToPoints(1.5)
                        dataWidth = Application.CentimetersToPoints(3.25)
                        dataWidth2 = Application.CentimetersToPoints(1.5)
                        spaceWidth = Application.CentimetersToPoints(0.5)
                    Case Else
                        firstWidth = Application.CentimetersToPoints(7.5)
                        remarkWidth = Application.CentimetersToPoints(1.5)
                        dataWidth = Application.CentimetersToPoints(3.25)
                        spaceWidth = Application.CentimetersToPoints(0.5)
                End Select
            Case 6, 7 '3 data col, size 11,11
                fontSize = 11
                Select Case formatIdentity
                    Case 1
                        firstWidth = Application.CentimetersToPoints(4.5)
                        remarkWidth = Application.CentimetersToPoints(1.5)
                        dataWidth = Application.CentimetersToPoints(3.25)
                        dataWidth2 = Application.CentimetersToPoints(1.5)
                        spaceWidth = Application.CentimetersToPoints(0.5)
                    Case Else
                        firstWidth = Application.CentimetersToPoints(5.1)
                        remarkWidth = Application.CentimetersToPoints(1.5)
                        dataWidth = Application.CentimetersToPoints(2.8)
                        spaceWidth = Application.CentimetersToPoints(0.5)
                End Select
            Case 8, 9 '4 data col, size 9,9
                fontSize = 9
                Select Case formatIdentity
                    Case 2
                        firstWidth = Application.CentimetersToPoints(4)
                        remarkWidth = Application.CentimetersToPoints(0.8)
                        dataWidth = Application.CentimetersToPoints(3)
                        dataWidth2 = Application.CentimetersToPoints(1.8)
                        spaceWidth = Application.CentimetersToPoints(0.2)
                    Case 3
                        firstWidth = Application.CentimetersToPoints(4)
                        remarkWidth = Application.CentimetersToPoints(0.8)
                        dataWidth = Application.CentimetersToPoints(1.8)
                        dataWidth2 = Application.CentimetersToPoints(3)
                        spaceWidth = Application.CentimetersToPoints(0.2)
                    Case Else
                        firstWidth = Application.CentimetersToPoints(5)
                        remarkWidth = Application.CentimetersToPoints(0.8)
                        dataWidth = Application.CentimetersToPoints(2.4)
                        spaceWidth = Application.CentimetersToPoints(0.2)
                End Select
            Case 10, 11 '5 data col, size 9,8
                If mergedTable Then
                    fontSize = 8
                Else
                    fontSize = 9
                End If
                firstWidth = Application.CentimetersToPoints(4.1)
                remarkWidth = Application.CentimetersToPoints(0.8)
                dataWidth = Application.CentimetersToPoints(2.1)
                spaceWidth = Application.CentimetersToPoints(0.15)
            Case 12, 13 '6 data col, size 9,7
                If orient = "P" Then
                    fontSize = 7
                Else
                    fontSize = 9
                End If
                Select Case formatIdentity
                    Case 2
                        firstWidth = Application.CentimetersToPoints(3.6)
                        remarkWidth = Application.CentimetersToPoints(0.8)
                        dataWidth = Application.CentimetersToPoints(1.8)
                        dataWidth2 = Application.CentimetersToPoints(1.4)
                        spaceWidth = Application.CentimetersToPoints(0.12)
                    Case Else
                        If orient = "P" Then
                            firstWidth = Application.CentimetersToPoints(3.8)
                            remarkWidth = Application.CentimetersToPoints(0.8)
                            dataWidth = Application.CentimetersToPoints(1.8)
                            spaceWidth = Application.CentimetersToPoints(0.12)
                        Else
                            firstWidth = Application.CentimetersToPoints(4.7)
                            remarkWidth = Application.CentimetersToPoints(1)
                            dataWidth = Application.CentimetersToPoints(3)
                            spaceWidth = Application.CentimetersToPoints(0.2)
                        End If
                End Select
            Case 14, 15 '7 data col, size 8,7
                If orient = "P" Then
                    fontSize = 7
                    firstWidth = Application.CentimetersToPoints(3.98)
                    remarkWidth = Application.CentimetersToPoints(0.8)
                    dataWidth = Application.CentimetersToPoints(1.5)
                    spaceWidth = Application.CentimetersToPoints(0.12)
                Else
                    fontSize = 8
                    firstWidth = Application.CentimetersToPoints(5)
                    remarkWidth = Application.CentimetersToPoints(1)
                    dataWidth = Application.CentimetersToPoints(2.5)
                    spaceWidth = Application.CentimetersToPoints(0.2)
                End If
            Case 16, 17 '8 data col, , size 8,7
                If orient = "P" Then
                    fontSize = 7
                Else
                    fontSize = 8
                End If
                Select Case formatIdentity
                    Case 2
                        firstWidth = Application.CentimetersToPoints(3.56)
                        remarkWidth = Application.CentimetersToPoints(0.8)
                        dataWidth = Application.CentimetersToPoints(1.3)
                        dataWidth2 = Application.CentimetersToPoints(1)
                        spaceWidth = Application.CentimetersToPoints(0.12)
                    Case Else
                        If orient = "P" Then
                            firstWidth = Application.CentimetersToPoints(3.96)
                            remarkWidth = Application.CentimetersToPoints(0.8)
                            dataWidth = Application.CentimetersToPoints(1.3)
                            spaceWidth = Application.CentimetersToPoints(0.12)
                        Else
                            firstWidth = Application.CentimetersToPoints(6.3)
                            remarkWidth = Application.CentimetersToPoints(1)
                            dataWidth = Application.CentimetersToPoints(2)
                            spaceWidth = Application.CentimetersToPoints(0.2)
                        End If
                End Select
            Case 18, 19 '9 data col, , size 7
                fontSize = 7
                firstWidth = Application.CentimetersToPoints(4.1)
                remarkWidth = Application.CentimetersToPoints(1)
                dataWidth = Application.CentimetersToPoints(2)
                spaceWidth = Application.CentimetersToPoints(0.2)
            Case Else
                fontSize = 7
                formatIdentity = 100
        End Select
        .Range.Font.Size = fontSize

        Select Case formatIdentity
            Case 10
                .Columns(1).PreferredWidth = Application.CentimetersToPoints(3.9)  'first col
                For i = 2 To colCount - 1 Step 2
                    .Columns(i).PreferredWidth = Application.CentimetersToPoints(3.5)  'data col
                Next i
                For j = 3 To colCount - 2 Step 2
                    .Columns(j).PreferredWidth = Application.CentimetersToPoints(0.3)  'space col
                Next j
                .Columns(colCount).PreferredWidth = Application.CentimetersToPoints(1)  'last col
            Case 100
                For j = 1 To colCount  'set width for space cols
                    .Columns(j).PreferredWidth = wdDoc.Tables(tableIndex).Cell(1, 1).Width / colCount
                Next j
            Case Else
                For j = 3 + hasRemark To colCount - 1 Step 2 'set width for space cols
                    .Columns(j).PreferredWidth = spaceWidth   '6.22
                Next j
                For i = 2 + hasRemark To colCount Step 2 'set width for data cols
                    .Columns(i).PreferredWidth = dataWidth
                Next i
                Select Case formatIdentity 'reset width for special data cols
                    Case 1
                        .Columns(colCount).PreferredWidth = dataWidth2
                    Case 2, 3
                        .Columns(colCount).PreferredWidth = dataWidth2
                        .Columns((colCount + hasRemark) / 2).PreferredWidth = dataWidth2
                End Select
                'set width for data/remark cols
                firstWidth = firstWidth - Application.CentimetersToPoints(headLevel)
                If hasRemark > 0 Then
                    .Columns(1).PreferredWidth = firstWidth
                    .Columns(2).PreferredWidth = remarkWidth
                Else
                    .Columns(1).PreferredWidth = (firstWidth + remarkWidth)
                End If
        End Select

        .LeftPadding = 0
        .RightPadding = 0
        .Range.Paragraphs.SpaceBefore = 0
        .Range.Paragraphs.SpaceAfter = 0
        .Range.Paragraphs.LineSpacingRule = wdLineSpaceSingle
        .Range.Font.NameFarEast = "微软雅黑"
        .Range.Font.NameAscii = "Arial"
        .Range.Font.NameOther = "Arial"

        If fontSize < 6 Then
            fontSize = 6
        End If
        If fontSize > 11 Then
            fontSize = 11
        End If
        If language = 0 Then
            rowHeight = Application.CentimetersToPoints(0.37 + (fontSize - 6) * 0.06)
        ElseIf language = 1 Then
            rowHeight = Application.CentimetersToPoints(0.24 + (fontSize - 6) * 0.044)
        End If

        For k = 1 To .Rows.count
            If .Rows(k).Height > 3 Then
                .Rows(k).Height = rowHeight
                .Rows(k).HeightRule = wdRowHeightAtLeast
            Else
                .Rows(k).HeightRule = wdRowHeightExactly
            End If
        Next k
    End With

    Err.Clear
End Sub

Sub Set_After_Copy()
    Dim mappedSheets As Variant
    Dim count, usedRow As Integer

    Sheets("Temp").Cells.Delete

    With Sheets("Main")
        .Select
        usedRow = .[G65536].End(xlUp).Row
        If usedRow > resultRow Then
            mappedSheets = Application.Transpose(Application.Index(.Range("G" & resultRow & ":G" & usedRow).Value, , 1))
            For j = 1 To UBound(mappedSheets)
                If mappedSheets(j) <> "" Then
                    count = Look_For_Item_Count(mappedSheets, mappedSheets(j))
                    If count > 1 Then
                        .Cells(j + resultRow - 1, 7).Interior.Color = 65535
                    End If
                End If
            Next j
        End If

        usedRow = .[AA65536].End(xlUp).Row
         .Range("AA1" & ":AE" & usedRow).ClearContents
        .Cells(1, 27) = .Cells(10, 5)
        usedRow = .[A65536].End(xlUp).Row
        .Range("A" & resultRow & ":E" & usedRow).Copy
        .Range("AA2" & ":AE" & (usedRow - resultRow + 2)).Select
        .Paste
        .Range("F15:F15").Select
    End With
    Application.CutCopyMode = False
End Sub

Sub Select_File()
    With Application.FileDialog(msoFileDialogFilePicker)
        .AllowMultiSelect = False
        .Filters.Clear
        .Filters.Add "Word Files", "*.docx;*.doc"

        If .Show = -1 Then
            Sheets("Main").Cells(9, 6) = .SelectedItems(1)
        End If
    End With
End Sub

Sub Show_Hide_Tables()
    On Error Resume Next
    Dim wdApp As Word.Application
    Dim wdDoc As Word.Document
    Dim docFile As String
    Set wdApp = New Word.Application
    docFile = Sheets("Main").Cells(9, 6)
    Set wdDoc = wdApp.Documents.Open(docFile)

    If wdDoc Is Nothing Then
        MsgBox "Please select a target word file!", vbOKOnly + vbInformation, "Error!"
        Exit Sub
    End If

    For j = 1 To wdDoc.Tables.count
        If wdDoc.Tables(j).Title <> "" Then
            With wdDoc.Tables(j).Cell(1, 1)
                If .Borders.OutsideLineStyle = wdLineStyleNone Then
                    .Borders.OutsideLineStyle = wdLineStyleSingle
                    .Borders.OutsideColor = wdColorYellow
                    Sheets("Main").Buttons("btnSwitch").Font.ColorIndex = 6
                Else
                    .Borders.OutsideLineStyle = wdLineStyleNone
                    Sheets("Main").Buttons("btnSwitch").Font.ColorIndex = xlAutomatic
                End If
            End With
        End If
    Next j

    Err.Clear
    wdDoc.Close
    wdApp.Quit
    Set wdDoc = Nothing
    Set wdApp = Nothing
End Sub

Sub Get_Sheet_List()
    On Error Resume Next
    Dim wdApp As Word.Application
    Dim wdDoc As Word.Document
    Dim docFile As String
    Set wdApp = New Word.Application
    docFile = Sheets("Main").Cells(9, 6)
    Set wdDoc = wdApp.Documents.Open(docFile)
    If wdDoc Is Nothing Then
        MsgBox "Please select a target word file!", vbOKOnly + vbInformation, "Error!"
        Exit Sub
    End If

    ReDim tableTitles(wdDoc.Tables.count) As String
    For j = 1 To wdDoc.Tables.count
        tableTitles(j - 1) = wdDoc.Tables(j).Title
    Next j
    wdDoc.Close
    wdApp.Quit
    Set wdDoc = Nothing
    Set wdApp = Nothing

    Dim sheetCount As Integer
    Dim formatSettings As Variant
    With Sheets("Main")
        sheetCount = .[A65536].End(xlUp).Row
        If sheetCount >= resultRow Then
            formatSettings = .Range("A" & resultRow & ":E" & sheetCount).Value
            .Range("A" & resultRow & ":E" & sheetCount).ClearContents
        End If

        For i = 4 To Sheets.count
            .Cells(resultRow + i - 4, 1) = Sheets(i).Name
            If IsNumeric(Application.Match(Sheets(i).Name, tableTitles, 0)) = True Then
                .Cells(resultRow + i - 4, 2) = "Y"
            End If
            For j = 1 To UBound(formatSettings)
                If Sheets(i).Name = formatSettings(j, 1) Then
                    .Cells(resultRow + i - 4, 3) = formatSettings(j, 3)
                    .Cells(resultRow + i - 4, 4) = formatSettings(j, 4)
                    .Cells(resultRow + i - 4, 5) = formatSettings(j, 5)
                    Exit For
                End If
            Next j
        Next i
    End With

    Err.Clear
End Sub

Sub Generate_Sheets_And_Word()
    On Error Resume Next
    Dim sheetsNames, docPath As String
    Dim wdApp As Word.Application
    sheetsNames = Sheets("Main").Cells(2, 1)
    Sheets("Main").Cells(8, 6) = ""
    docPath = ThisWorkbook.Path & "\" & Year(Now) & Month(Now) & Day(Now) & Hour(Now) & Minute(Now) & Second(Now) & ".docx"

    Set wdApp = New Word.Application
    wdApp.Documents.Add
    If Len(sheetsNames) > 0 Then
        Dim sheetsList() As String
        sheetsList = Split(sheetsNames, "/")

        With wdApp.ActiveDocument.PageSetup
            .LeftMargin = Application.CentimetersToPoints(2.5)
            .RightMargin = Application.CentimetersToPoints(2.5)
            .TopMargin = Application.CentimetersToPoints(2.8)
            .BottomMargin = Application.CentimetersToPoints(2.8)
        End With

        With wdApp.Selection
            For i = 0 To UBound(sheetsList)
                If Trim(sheetsList(i)) <> "" Then
                    .Tables.Add .Range, 1, 1
                    .Comments.Add .Range, sheetsList(i)
                    .MoveDown Unit:=wdLine, count:=1
                    .TypeParagraph
                    .TypeParagraph
                    .TypeParagraph
                    Set NewSheet = Sheets(sheetsList(i))
                    If Err <> 0 Then
                        Err.Clear
                        Sheets.Add(After:=Sheets(Sheets.count)).Name = sheetsList(i)
                    End If
                End If
            Next i
        End With

        With wdApp.ActiveDocument
            For j = 1 To .Tables.count
                .Tables(j).Borders.OutsideLineStyle = wdLineStyleSingle
                .Tables(j).Borders.OutsideColor = wdColorYellow
                .Tables(j).Title = sheetsList(j - 1)
            Next j
        End With
    Else
        With wdApp.ActiveDocument.PageSetup
            .LeftMargin = Application.CentimetersToPoints(2.5)
            .RightMargin = Application.CentimetersToPoints(2.5)
            .TopMargin = Application.CentimetersToPoints(2.8)
            .BottomMargin = Application.CentimetersToPoints(2.8)
        End With

        With wdApp.Selection
            For i = 4 To Sheets.count
                .Tables.Add .Range, 1, 1
                .Comments.Add .Range, Sheets(i).Name
                .MoveDown Unit:=wdLine, count:=1
                .TypeParagraph
                .TypeParagraph
                .TypeParagraph
            Next i
        End With

        With wdApp.ActiveDocument
            For j = 1 To .Tables.count
                .Tables(j).Borders.OutsideLineStyle = wdLineStyleSingle
                .Tables(j).Borders.OutsideColor = wdColorYellow
                .Tables(j).Title = Sheets(j + 3).Name
            Next j
        End With
    End If

    wdApp.ActiveDocument.SaveAs docPath
    wdApp.Quit
    Set wdApp = Nothing
    Sheets("Main").Cells(8, 6) = docPath
    Err.Clear
End Sub

Sub Add_Button()
    On Error Resume Next
    For i = 4 To Sheets.count
        With Sheets(i)
            .Buttons("Line1").Delete
            .Buttons("Line2").Delete
            .Buttons("Line3").Delete
            .Buttons("Line4").Delete
            .Buttons("Line5").Delete
            .Buttons.Add(1000, 10, 80, 15).Name = "Line1"
            .Buttons.Add(1000, 30, 80, 15).Name = "Line2"
            .Buttons.Add(1000, 50, 80, 15).Name = "Line3"
            .Buttons.Add(1000, 70, 80, 15).Name = "Line4"
            .Buttons.Add(1000, 90, 80, 15).Name = "Line5"
            .Buttons("Line1").OnAction = "Draw_Line1"
            .Buttons("Line2").OnAction = "Draw_Line2"
            .Buttons("Line3").OnAction = "Draw_Line3"
            .Buttons("Line4").OnAction = "Draw_Line4"
            .Buttons("Line5").OnAction = "Draw_Line5"
            .Buttons("Line1").Text = "Solid Line"
            .Buttons("Line2").Text = "Dotted Line"
            .Buttons("Line3").Text = "Solid&Dotted Line"
            .Buttons("Line4").Text = "Double Solid Line"
            .Buttons("Line5").Text = "Back to Main"
            .Buttons.Group
        End With
        Err.Clear
    Next i
End Sub

Sub Draw_Line1()
    Selection.Borders(xlEdgeBottom).LineStyle = xlContinuous
End Sub

Sub Draw_Line2()
    Selection.Borders(xlEdgeBottom).LineStyle = xlDash
End Sub

Sub Draw_Line3()
    Dim r, c As Integer
    r = Selection.Row + 1
    c = Selection.Column
    If ActiveSheet.Rows(r).rowHeight > 3 Then
        ActiveSheet.Rows(r).Insert Shift:=xlDown, CopyOrigin:=xlFormatFromLeftOrAbove
    End If
    ActiveSheet.Cells(r, c).Borders(xlEdgeTop).LineStyle = xlDash
    ActiveSheet.Cells(r, c).Borders(xlEdgeBottom).LineStyle = xlContinuous
    ActiveSheet.Rows(r).rowHeight = 3
End Sub

Sub Draw_Line4()
    Selection.Borders(xlEdgeBottom).LineStyle = xlDouble
End Sub
Sub Draw_Line5()
   Worksheets("Main").Activate
End Sub

Sub auto_open()
    Dim objs As Object
    Set objs = GetObject("winmgmts:").ExecQuery("select * from win32_computersystem")

    For Each obj In objs
        If obj.domain <> "cn.kworld.kpmg.com" Then
            On Error Resume Next
            Workbooks.Close
        End If
        Exit For
    Next obj

    Set objs = Nothing
End Sub

Sub Set_To_Chinese()
    Sheets("Main").Cells(10, 5) = 0
End Sub

Sub Set_To_English()
    Sheets("Main").Cells(10, 5) = 1
End Sub

Function Look_For_Item_Count(ByVal arr As Variant, ByVal item As String) As Integer
    Dim count As Integer

    For Each obj In arr
        If item = obj Then
            count = count + 1
        End If
    Next obj

    Look_For_Item_Count = count
End Function
Sub transform()
Dim ob As Range
Dim findxobj As Range
Dim findyobj As Range
 For i = 5 To Sheets.count
rowi = Sheets(i).[A65536].End(xlUp).Row
coli = Sheets(i).Cells(1, Columns.count).End(xlToLeft).Column
Namei = Sheets(i).Name
rowall = Sheets("对照表").[A65536].End(xlUp).Row
With Sheets("对照表").Range("A1:A" & rowall)
Set findxobj = .Find(Namei, LookIn:=xlValues, LookAt:=xlWhole, SearchDirection:=xlNext)
Set findyobj = .Find(Namei, LookIn:=xlValues, LookAt:=xlWhole, SearchDirection:=xlPrevious)
If Not findxobj Is Nothing Then
findx = .Find(Namei, LookIn:=xlValues, LookAt:=xlWhole, SearchDirection:=xlNext).Row
If Not findyobj Is Nothing Then
findy = .Find(Namei, LookIn:=xlValues, LookAt:=xlWhole, SearchDirection:=xlPrevious).Row


For x = 1 To rowi
 For y = 1 To coli
 tar = Sheets(i).Cells(x, y)
With Sheets("对照表")
Set ob = .Range("B" & findx & ":B" & findy).Find(tar, LookIn:=xlValues, LookAt:=xlWhole, SearchDirection:=xlNext)
If Not ob Is Nothing Then
 findbr = ob.Row
Sheets(i).Cells(x, y) = .Range("C" & findbr)
End If
End With

Next
Next
End If
End If

End With



Next
MsgBox "完成"
End Sub

Sub CreateTab()
j = 1
For i = 4 To Sheets.count
rowi = Sheets(i).[A65536].End(xlUp).Row
coli = Sheets(i).Cells(1, Columns.count).End(xlToLeft).Column



For x = 1 To rowi
 For y = 1 To coli
 tar = Sheets(i).Cells(x, y)
 Namei = Sheets(i).Name
With Sheets("对照表")

If tar Like "*[一-龥]*" Then
Sheets("对照表").Cells(j + 4, 1) = Namei
Sheets("对照表").Cells(j + 4, 2) = tar
j = j + 1
End If
End With

Next
Next


Next
MsgBox "完成"
End Sub

Sub save()

ThisWorkbook.SaveCopyAs ThisWorkbook.Path & "\" & Split(ThisWorkbook.Name, ".xls")(0) & "中文" & ".xlsm"
End Sub


Sub addsheet()
Application.DisplayAlerts = False
save

On Error Resume Next
If ActiveWorkbook.Sheets("对照表") Is Nothing Then
 Sheets.Add(After:=Sheets(3)).Name = "对照表"
 With Sheets("对照表")
            .Buttons("Creat Translation").Delete
            .Buttons("TranslateToEnglish").Delete
            .Buttons.Add(20, 10, 180, 15).Name = "CreatTranslation"
            .Buttons.Add(20, 30, 180, 15).Name = "TranslateToEnglish"
            .Buttons("CreatTranslation").OnAction = "CreateTab"
            .Buttons("TranslateToEnglish").OnAction = "transform"
            .Buttons("CreatTranslation").Text = "Create Translation"
            .Buttons("TranslateToEnglish").Text = "Translate To English"
            .Buttons.Group
        End With

Else
Sheets("对照表").Delete
Sheets.Add(After:=Sheets(3)).Name = "对照表"
 With Sheets("对照表")
            .Buttons("CreatTranslation").Delete
            .Buttons("TranslateToEnglish").Delete
            .Buttons.Add(20, 10, 180, 15).Name = "CreatTranslation"
            .Buttons.Add(20, 30, 180, 15).Name = "TranslateToEnglish"
            .Buttons("CreatTranslation").OnAction = "CreateTab"
            .Buttons("TranslateToEnglish").OnAction = "transform"
            .Buttons("CreatTranslation").Text = "Create Translation"
            .Buttons("TranslateToEnglish").Text = "Translate To English"
            .Buttons.Group
        End With
Err.Clear
End If


End Sub

Sub GetBlankPage(ByRef wdDoc As Word.Document)
Dim IsDelete As Boolean
Dim PageCount As Long
Dim rRange     As Range
Dim iInt     As Integer, DelCount As Integer
Dim tmpstr As String

    IsDelete = True
    PageCount = wdDoc.ActiveDocument.BuiltinDocumentProperties(wdPropertyPages)
    For iInt = 1 To PageCount
        '超过PageCount退出
        If iInt > PageCount Then Exit For

        '取每一页的内容
        If iInt = PageCount Then
            Set rRange = wdDoc.ActiveDocument.Range( _
                            Start:=wdDoc.ActiveDocument.Goto(wdGoToPage, wdGoToAbsolute, iInt).Start)
        Else
            Set rRange = wdDoc.ActiveDocument.Range( _
                            Start:=wdDoc.ActiveDocument.Goto(wdGoToPage, wdGoToAbsolute, iInt).Start, _
                            End:=wdDoc.ActiveDocument.Goto(wdGoToPage, wdGoToAbsolute, iInt + 1).Start _
                            )
        End If

        If Replace(rRange.Text, Chr(13), "") = "" Or Replace(rRange.Text, Chr(13), "") = Chr(12) Then
            tmpstr = tmpstr & "第 " & iInt & " 页是空页" & vbCrLf
            '删除?
            If IsDelete Then
                DelCount = DelCount + 1
                '删除空白页
                rRange.Text = Replace(rRange.Text, Chr(13), "")
                rRange.Text = ""
                '重算页数
                PageCount = wdDoc.ActiveDocument.BuiltinDocumentProperties(wdPropertyPages)
                If iInt <> PageCount Then
                    '页删除后，页码变化，重新检查当前页
                    iInt = iInt - 1
                Else
                    '最后一个空页
                    Set rRange = wdDoc.ActiveDocument.Range( _
                                    Start:=wdDoc.ActiveDocument.Goto(wdGoToPage, wdGoToAbsolute, PageCount - 1).Start, _
                                    End:=wdDoc.ActiveDocument.Goto(wdGoToPage, wdGoToAbsolute, PageCount + 1).Start _
                                    )
                    '如果是分页符,删除上一页中的换页符
                    If InStr(1, rRange.Text, Chr(12)) > 0 Then
                        rRange.Characters(InStr(1, rRange.Text, Chr(12))) = ""
                    Else
                        '没有分页符,通过选中后删除,最好不这样做，如果判断错误，有误删除的风险
                        Set rRange = wdDoc.ActiveDocument.Range( _
                                        Start:=wdDoc.ActiveDocument.Goto(wdGoToPage, wdGoToAbsolute, iInt).Start)
                        rRange.Select
                        Selection.Delete
                    End If
                    Exit For
                End If
            End If
        End If
    Next
End Sub


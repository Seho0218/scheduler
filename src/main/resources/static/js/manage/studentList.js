$(document).ready(function() {
    $('.save-button').click(function() {
        const teacherID = $(this).siblings('label').children('.teacherID').val();
        const studentID = $(this).parent('td').siblings('td').children('.studentID').val();

        const confirmation = confirm('담당교사를 변경하시겠습니까?');
        if (confirmation) {
            $.ajax({
                url: '/admin/api/changeTeacher',
                method: 'POST',
                data: {
                    teacherId : teacherID,
                    studentId : studentID
                },
                success: function() {
                    alert("변경되었습니다.");
                    location.reload();
                },
                error: function(xhr) {
                    alert(xhr.responseText);
                }
            });
        }
    });

    $('.delete-button').click(function() {
        const studentID = $(this).attr('value');
        const confirmation = confirm('정말 학생 정보를 삭제하시겠습니까?');
        if (confirmation) {
            $.ajax({
                url: '/manage/deleteStudentList',
                method: 'POST',
                data: { id : studentID },
                success: function(data) {
                    alert(data);
                    location.reload();
                },
                error: function(xhr) {
                    alert(xhr.responseText);
                }
            });
        }
    });
});
#!/usr/local/bin/perl -w

use Net::SMTP;

$subject = "tempeh address";
#$date = `date`;
if ( defined(@ARGV) ) {
         $subject = "Commit message Revision @ARGV";
}

$smtp = Net::SMTP->new('smtp.toucansurf.com');
$smtp->mail('yut45390@github.com');
$smtp->to('yut45390@github.co');
$smtp->data();
#$smtp->datasend("From nobody\@tempeh.maths.clrc.ac.uk $date\n"); 
#$smtp->datasend("Date: $date\n");
#$smtp->datasend("From: Tempeh start process <nobody\@tempeh.maths.clrc.ac.uk>\n");
$smtp->datasend("To: yut45390\@github.com\n");
$smtp->datasend("Subject: $subject\n");
$smtp->datasend("\n");

while (<stdin>) {
         $smtp->datasend($_);
}
$smtp->dataend();
$smtp->quit;


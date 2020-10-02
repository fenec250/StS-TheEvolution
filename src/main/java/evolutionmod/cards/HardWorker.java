package evolutionmod.cards;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.defect.ChannelAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.StrengthPower;
import evolutionmod.orbs.AbstractGene;
import evolutionmod.orbs.CentaurGene;
import evolutionmod.orbs.InsectGene;
import evolutionmod.patches.AbstractCardEnum;
import evolutionmod.powers.LavafolkFormPower;
import evolutionmod.powers.LymeanFormPower;

public class HardWorker
        extends BaseEvoCard {
    public static final String ID = "evolutionmod:HardWorker";
    public static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    public static final String IMG_PATH = "evolutionmod/images/cards/CentaurForm.png";
    private static final int COST = 1;
    private static final int STRENGTH_AMT = 1;
    private static final int UPGRADE_STRENGTH_AMT = 1;
    private static final int FORMS_STRENGTH_AMT = 1;

    public HardWorker() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION,
                CardType.POWER, AbstractCardEnum.EVOLUTION_BLUE,
                CardRarity.UNCOMMON, CardTarget.SELF);
        this.magicNumber = this.baseMagicNumber = STRENGTH_AMT;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(p, p,
                new StrengthPower(p, this.magicNumber)));
        if (!AbstractGene.isPlayerInThisForm(CentaurGene.ID)) {
            addToBot(new ChannelAction(new CentaurGene()));
        } else {
            AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(p, p,
                    new StrengthPower(p, FORMS_STRENGTH_AMT)));
        }
        if (this.upgraded) {
            if (!AbstractGene.isPlayerInThisForm(InsectGene.ID)){
                addToBot(new ChannelAction(new InsectGene()));
            } else {
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(p, p,
                        new StrengthPower(p, FORMS_STRENGTH_AMT)));
            }
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new HardWorker();
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeMagicNumber(UPGRADE_STRENGTH_AMT);
            this.rawDescription = UPGRADE_DESCRIPTION;
            this.initializeDescription();
        }
    }
}
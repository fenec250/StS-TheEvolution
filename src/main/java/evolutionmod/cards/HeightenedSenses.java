package evolutionmod.cards;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.defect.ChannelAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.RagePower;
import evolutionmod.actions.HeightenedSensesAction;
import evolutionmod.orbs.BeastGene;
import evolutionmod.orbs.HarpyGene;
import evolutionmod.patches.AbstractCardEnum;

public class HeightenedSenses
        extends BaseEvoCard {
    public static final String ID = "evolutionmod:HeightenedSenses";
    public static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    public static final String IMG_PATH = "evolutionmod/images/cards/HeightenedSenses.png";
    private static final int COST = 1;
    private static final int DRAW_AMT = 3;
    private static final int UPGRADED_DRAW_AMT = 1;
    private static final int FORM_RAGE_AMT = 3;
    private static final int UPGRADE_FORM_RAGE_AMT = 1;

    public HeightenedSenses() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION,
                CardType.SKILL, AbstractCardEnum.EVOLUTION_BLUE,
                CardRarity.UNCOMMON, CardTarget.SELF);
        this.magicNumber = this.baseMagicNumber = DRAW_AMT;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        // TODO: Check to do like ScrapeFollowUpAction
        AbstractDungeon.actionManager.addToBottom(new HeightenedSensesAction(p, this.magicNumber));
        formEffect(BeastGene.ID, () -> formEffect(HarpyGene.ID, () -> {
            int rage = this.upgraded ? FORM_RAGE_AMT + UPGRADE_FORM_RAGE_AMT : FORM_RAGE_AMT;
            addToBot(new ApplyPowerAction(p, p, new RagePower(p, rage)));
        }));
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeMagicNumber(UPGRADED_DRAW_AMT);
            this.rawDescription = UPGRADE_DESCRIPTION;
            this.initializeDescription();
        }
    }
}
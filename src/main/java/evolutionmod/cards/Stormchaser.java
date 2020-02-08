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
import com.megacrit.cardcrawl.powers.PlatedArmorPower;
import evolutionmod.orbs.BeastGene;
import evolutionmod.orbs.CentaurGene;
import evolutionmod.orbs.HarpyGene;
import evolutionmod.orbs.MerfolkGene;
import evolutionmod.patches.AbstractCardEnum;

public class Stormchaser
        extends CustomCard {
    public static final String ID = "evolutionmod:Stormchaser";
    public static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    public static final String IMG_PATH = "evolutionmod/images/cards/strike.png";
    private static final int COST = 1;
    private static final int UPGRADED_COST = 0;
    private static final int BLOCK_PER_CARD = 1;

    public Stormchaser() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION,
                CardType.POWER, AbstractCardEnum.EVOLUTION_BLUE,
                CardRarity.UNCOMMON, CardTarget.ENEMY);
        this.magicNumber = this.baseMagicNumber = BLOCK_PER_CARD;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {

//        addToBot(new ApplyPowerAction(p, p,
//                new PlatedArmorPower(p, this.magicNumber), this.magicNumber));

        AbstractDungeon.actionManager.addToBottom(new ChannelAction(new HarpyGene()));
        AbstractDungeon.actionManager.addToBottom(new ChannelAction(new MerfolkGene()));
    }

    @Override
    public AbstractCard makeCopy() {
        return new Stormchaser();
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.updateCost(UPGRADED_COST);
        }
    }
}
